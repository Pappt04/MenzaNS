package com.pappt04.menzans.views

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.views.navigation.MainNavigationDrawer
import com.pappt04.menzans.data.local.FileContainer.CardHolderFileName
import com.pappt04.menzans.data.local.datastore.SettingsDataStoreManager
import com.pappt04.menzans.data.consts.DummyData
import com.pappt04.menzans.geolocation.GeofenceManager
import com.pappt04.menzans.notifications.createChannel
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import com.pappt04.menzans.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

// Temporary global UserID for backward compatibility during migration
var UserID: com.pappt04.menzans.models.UserIDString = com.pappt04.menzans.models.UserIDString("")

class MainActivity : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 1004
    private val ALL_LOCATION_PERMISSIONS = 1010

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainViewModel.initialize()

        setContent {
            val context = LocalContext.current
            val state by mainViewModel.uiState.collectAsState()

            // Keep global UserID in sync for backward compatibility
            LaunchedEffect(state.userId) {
                UserID.userid = state.userId
            }

            // Register geofences once loaded
            LaunchedEffect(state.isLoaded) {
                if (state.isLoaded) {
                    val geofenceManager = GeofenceManager(context)
                    for (geofence in DummyData.LANDMARK_DATA) {
                        geofenceManager.addGeofence(
                            geofence.key,
                            geofence.location,
                            geofence.radiusInMeters,
                            geofence.expirationTimeInMillis
                        )
                    }
                    geofenceManager.registerGeofence()
                }
            }

            MenzaNSTheme(darkTheme = state.darkTheme, dynamicColor = state.materialYouTheme) {
                requestAllPermissions()
                createChannel(context)

                if (state.isLoaded) {
                    val darkTheme = remember(state.darkTheme) { mutableStateOf(state.darkTheme) }
                    val materialTheme = remember(state.materialYouTheme) { mutableStateOf(state.materialYouTheme) }
                    val onBudgetPricing = remember(state.onBudgetPricing) { mutableStateOf(state.onBudgetPricing) }

                    val firstWelcome = remember { mutableStateOf(true) }
                    val files: Array<String> = context.fileList()
                    if (CardHolderFileName in files) {
                        firstWelcome.value = false
                    }

                    val savedMeals = remember { SnapshotStateList<Int>() }
                    LaunchedEffect(state.savedMeals) {
                        savedMeals.clear()
                        savedMeals.addAll(state.savedMeals)
                    }

                    val settingsDataManager = remember { SettingsDataStoreManager(context) }

                    MainNavigationDrawer(
                        darkTheme,
                        materialTheme,
                        onBudgetPricing,
                        firstWelcome,
                        settingsDataManager,
                        savedMeals
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    private fun requestAllPermissions() {
        requestAllLocationPermission()
        requestNotificationLocationPermission()
    }

    private fun requestAllLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            ALL_LOCATION_PERMISSIONS
        )
    }

    private fun requestNotificationLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }
}
