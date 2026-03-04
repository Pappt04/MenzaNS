package com.pappt04.menzans.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.views.navigation.MainNavigationDrawer
import com.pappt04.menzans.data.consts.GeofenceConstants
import com.pappt04.menzans.geolocation.GeofenceManager
import com.pappt04.menzans.notifications.createChannel
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import com.pappt04.menzans.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_PERMISSION_CODE = 1004
    private val FOREGROUND_LOCATION_PERMISSIONS = 1010
    private val BACKGROUND_LOCATION_PERMISSION = 1011

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainViewModel.initialize()
        val splashStartTime = SystemClock.elapsedRealtime()
        splashScreen.setKeepOnScreenCondition {
            !mainViewModel.uiState.value.isLoaded ||
              SystemClock.elapsedRealtime() - splashStartTime < 500L
        }

        setContent {
            val context = LocalContext.current
            val state by mainViewModel.uiState.collectAsState()

            // Register geofences once loaded
            LaunchedEffect(state.isLoaded) {
                if (state.isLoaded) {
                    val geofenceManager = GeofenceManager(context)
                    for (geofence in GeofenceConstants.LANDMARKS) {
                        geofenceManager.addGeofence(
                            geofence.key,
                            geofence.location,
                            geofence.radiusInMeters,
                            geofence.expirationTimeInMillis,
                        )
                    }
                    geofenceManager.registerGeofence()
                }
            }

            MenzaNSTheme(darkTheme = state.darkTheme, dynamicColor = state.materialYouTheme) {
                requestAllPermissions()
                createChannel(context)

                if (state.isLoaded) {
                    MainNavigationDrawer(mainViewModel)
                } else {
                    // Splash screen API keeps the system splash visible until isLoaded is true
                }
            }
        }
    }

    private fun requestAllPermissions() {
        requestAllLocationPermission()
        requestNotificationLocationPermission()
    }

    private fun requestAllLocationPermission() {
        // On Android 11+ ACCESS_BACKGROUND_LOCATION must be requested separately,
        // after the user has already granted foreground location.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            FOREGROUND_LOCATION_PERMISSIONS,
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FOREGROUND_LOCATION_PERMISSIONS &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_PERMISSION,
            )
        }
    }

    private fun requestNotificationLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE,
            )
        }
    }
}
