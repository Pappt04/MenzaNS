package com.pappt04.menzans

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.key
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import java.text.SimpleDateFormat
import java.util.Date

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    public val TAG = "GeofenceBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) } ?: return

        if (geofencingEvent.hasError()) {
            val errorMassage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMassage)
            return
        }

        val alertString = "Geofence Alert :" +
                " Trigger ${geofencingEvent.triggeringGeofences}" +
                " Transition ${geofencingEvent.geofenceTransition}"
        Log.d(
            TAG,
            alertString
        )

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                //val sdf = SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z")
                val sdf = SimpleDateFormat("HH:mm")
                val currentDateAndTime = sdf.format(Date())
                context.openFileOutput(DummyData.FileGeoFenceEntered, Context.MODE_PRIVATE).use {
                    it.write(currentDateAndTime.toByteArray())
                }
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val sdf = SimpleDateFormat("HH:mm")
                val currentDateAndTime = sdf.format(Date())
                var s1 = ""
                val files: Array<String> = context.fileList()

                if (DummyData.FileGeoFenceEntered in files) {
                    context.openFileInput(DummyData.FileGeoFenceEntered).bufferedReader()
                        .useLines { lines ->
                            lines.fold("") { some, text ->
                                s1 = "$some$text"
                                s1
                            }
                        }
                }
                notificationManager.sendAteMealNotification(context,currentDateAndTime,s1)
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {

            }
        }
    }

}