package com.pappt04.menzans.geolocation

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.DummyData.MealSample
import com.pappt04.menzans.data.DummyData.datetypeclock
import com.pappt04.menzans.data.DummyData.datetypedate
import com.pappt04.menzans.data.DummyData.datetypemonth
import com.pappt04.menzans.data.EatingStatisticsData
import com.pappt04.menzans.data.FileDAO
import com.pappt04.menzans.data.MealData
import com.pappt04.menzans.statistics.StatisticsFileDAO
import com.pappt04.menzans.data.Uitext
import com.pappt04.menzans.UserID
import com.pappt04.menzans.notifications.sendAteMealNotification
import com.pappt04.menzans.notifications.sendAutomaticDeductNotification
import com.pappt04.menzans.data.sendEnterEvent
import com.pappt04.menzans.data.sendExitEvent
import java.time.LocalDate
import java.util.Date
import kotlin.math.abs

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "GeofenceBroadcastReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.i(TAG, "Activated")
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

                Log.i(TAG,"GEOFENCE ENTERED")
                val currentTime = datetypeclock.format(Date())
                context.openFileOutput(DummyData.FileGeoFenceEntered, Context.MODE_PRIVATE).use {
                    it.write(currentTime.toByteArray())
                }

                sendEnterEvent(UserID.userid, datetypedate.format(Date()),currentTime,context)

            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.i(TAG,"GEOFENCE EXITED")
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

                    if(UserID.userid != "")
                        sendExitEvent(
                            UserID.userid,timeExited,
                            findEngMeal(correctmeal.name),context )

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
                val currentTime = datetypeclock.format(Date())

                sendEnterEvent(UserID.userid, datetypedate.format(Date()),currentTime,context)
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

            var dao= FileDAO(context, DummyData.FileNames[mealIndex])

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

/**
 * Returns the meals english name
 */
fun findEngMeal(type: Uitext): String
{
    var i = 0
    for (m in DummyData.MealSample) {
        if (m.name == type) {
            return DummyData.engmeals[i]
        }
        i++
    }
    return ""
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
