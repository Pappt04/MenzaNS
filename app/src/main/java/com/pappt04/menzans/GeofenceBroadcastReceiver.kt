package com.pappt04.menzans

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.pappt04.menzans.DummyData.MealSample
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "GeofenceBroadcastReceiver"

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
                val timeEntered = sdf.format(Date())
                var timeExited = ""
                val files: Array<String> = context.fileList()

                if (DummyData.FileGeoFenceEntered in files) {
                    context.openFileInput(DummyData.FileGeoFenceEntered).bufferedReader()
                        .useLines { lines ->
                            lines.fold("") { some, text ->
                                timeExited = "$some$text"
                                timeExited
                            }
                        }
                }

                val enteredsplit = timeEntered.split(":").toTypedArray()
                val exitedsplit = timeExited.split(":").toTypedArray()


                val hourdiff: Int = abs(enteredsplit[0].toInt() - exitedsplit[0].toInt())
                val mindiff: Int = abs(enteredsplit[1].toInt() - exitedsplit[1].toInt())
                val alldiff: Int = hourdiff * 60 + mindiff

                if (alldiff > DummyData.EATING_SPEED_TRESHOLD) {
                    automaticallyDeductToken(context, enteredsplit, exitedsplit)
                    notificationManager.sendAutomaticDeductNotification(context, alldiff)
                } else {
                    notificationManager.sendAteMealNotification(
                        context,
                        timeEntered,
                        timeExited
                    )
                }
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {

            }
        }
    }

    private fun automaticallyDeductToken(
        context: Context,
        enteredsplit: Array<String>,
        exitedsplit: Array<String>
    ) {
        var correctMeal = 0
        for (mealdata in MealSample) {
            correctMeal++
            if (mealdata.start_hour <= (enteredsplit[0].toInt()) && mealdata.end_hour >= (exitedsplit[0].toInt())
            ) {
                //Maybe it should just check entered time
                val currentTokens =
                    readFromFile(context, DummyData.FileNames[correctMeal])
                saveToFile(context, DummyData.FileNames[correctMeal], currentTokens.toInt() - 1)
            }
        }
    }
}