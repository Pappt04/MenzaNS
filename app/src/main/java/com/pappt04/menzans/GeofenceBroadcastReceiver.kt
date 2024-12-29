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
import com.pappt04.menzans.DummyData.datetypeclock
import com.pappt04.menzans.DummyData.datetypemonth
import java.time.LocalDate
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
                val currentDateAndTime = datetypeclock.format(Date())
                context.openFileOutput(DummyData.FileGeoFenceEntered, Context.MODE_PRIVATE).use {
                    it.write(currentDateAndTime.toByteArray())
                }
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val timeExited = datetypeclock.format(Date())
                var timeEntered = ""
                val files: Array<String> = context.fileList()

                if (DummyData.FileGeoFenceEntered in files) {
                    context.openFileInput(DummyData.FileGeoFenceEntered).bufferedReader()
                        .useLines { lines ->
                            lines.fold("") { some, text ->
                                timeEntered = "$some$text"
                                timeEntered
                            }
                        }
                }

                val enteredsplit = timeEntered.split(":").toTypedArray()
                val exitedsplit = timeExited.split(":").toTypedArray()

                val alldiff: Int = calculateTimeDifference(enteredsplit, exitedsplit)

                val correctmeal = calculateCorrectMeal(timeEntered, timeExited)

                if (alldiff > DummyData.AUTOMATIC_EATING_SPEED_TRESHOLD && correctmeal!= null) {
                    automaticallyDeductToken(context, timeEntered, timeExited, correctmeal)
                    notificationManager.sendAutomaticDeductNotification(context, alldiff,correctmeal)
                } else if (correctmeal!=null /*&& alldiff >= DummyData.DWELL_TRESHOLD*/) {
                    notificationManager.sendAteMealNotification(
                        context,
                        timeEntered,
                        timeExited,
                        correctmeal
                    )
                }
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {

            }
        }
    }

    private fun automaticallyDeductToken(
        context: Context,
        timeEntered: String,
        timeExited: String,
        mealdata: MealData?
    ) {
        if(mealdata!= null){
            var mealIndex=0
            for(m in MealSample)
            {
                if(mealdata == m)
                    break
                mealIndex++
            }

            var dao= FileDAO(context,DummyData.FileNames[mealIndex])

            val currentTokens = dao.readFromFile()
            dao.saveToFile(currentTokens.toInt()-1,true)

            val statisticsMeal = EatingStatisticsData(
                LocalDate.now(),
                timeEntered,
                timeExited,
                mealdata.name
            )
            var fdao= StatisticsFileDAO(context, datetypemonth.format(Date()))
            fdao.appendToStatisticsFile(statisticsMeal)
        }
    }
}


fun calculateCorrectMeal(
    timeEntered: String,
    timeExited: String
): MealData? {
    val enteredsplit = timeEntered.split(":").toTypedArray()
    val exitedsplit = timeExited.split(":").toTypedArray()

    for (mealdata in MealSample) {
        if (mealdata.start_hour <= (enteredsplit[0].toInt()) && mealdata.end_hour >= (exitedsplit[0].toInt()))
            return mealdata
    }
    return null
}

fun calculateTimeDifference(enteredsplit: Array<String>, exitedsplit: Array<String>): Int {

    val hourdiff: Int = abs(enteredsplit[0].toInt() - exitedsplit[0].toInt())
    val mindiff: Int = abs(enteredsplit[1].toInt() - exitedsplit[1].toInt())

    return hourdiff * 60 + mindiff
}
