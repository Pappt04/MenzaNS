package com.pappt04.menzans

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.DummyData.CardHolderFileName
import com.pappt04.menzans.ui.theme.MenzaNSTheme


class MainActivity : ComponentActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 1004
    private val ALL_LOCATION_PERMISSIONS = 1010

    private lateinit var geofenceManager: GeofenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MenzaNSTheme {
                val context = LocalContext.current

                geofenceManager = GeofenceManager(context)

                requestAllPermissions()
                createChannel(context)


                val files: Array<String> = context.fileList()
                var stemp = ""
                if (CardHolderFileName in files) {
                    context.openFileInput(CardHolderFileName).bufferedReader()
                        .useLines { lines ->
                            lines.fold("") { some, text ->
                                stemp = "$some$text"
                                stemp
                            }
                        }
                } else {
                    stemp = ",,,,,,,,"
                    context.openFileOutput(CardHolderFileName, Context.MODE_PRIVATE).use {
                        it.write(stemp.toByteArray())
                    }
                }
                val splitstring: List<String> = stemp.split(",")
                MainNavigationDrawer(splitstring)

            }
        }
    }

    fun requestAllPermissions() {
        requestAllLocationPermission()
        requestNotifcationLocationPermission()
    }

    fun requestAllLocationPermission() {
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

    fun requestNotifcationLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }


    @Deprecated(
        "This method has been deprecated in favor of using the Activity Result API\n" +
                " which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n" +
                "contracts for common intents available in\n" +
                "{@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n" +
                "testing, and allow receiving results in separate, testable classes independent from your activity. Use\n" +
                "{@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing\n" +
                "in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and\n" +
                "handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}."
    )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_LOCATION_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Haha W", Toast.LENGTH_SHORT).show()
                    for(geofence in DummyData.LANDMARK_DATA){
                        geofenceManager.addGeofence(
                            geofence.key,
                            geofence.location,
                            geofence.radiusInMeters,
                            geofence.expirationTimeInMillis
                        )
                    }
                    geofenceManager.registerGeofence()
                } else {
                    //TODO NOTHING?
                }
            }

            NOTIFICATION_PERMISSION_CODE -> {
                //TODO
            }

        }
    }
}