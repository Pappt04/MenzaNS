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
import com.pappt04.menzans.data.consts.CalendarData
import com.pappt04.menzans.data.consts.CalendarData.dateFormat
import com.pappt04.menzans.data.consts.CalendarData.timeFormat
import com.pappt04.menzans.data.consts.GeofenceConstants
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.data.consts.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.models.MealData
import com.pappt04.menzans.models.SettingsPreferences
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.notifications.sendAteMealNotification
import com.pappt04.menzans.notifications.sendAutomaticDeductNotification
import com.pappt04.menzans.repository.GeofenceRepository
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.SettingsRepository
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.util.Date
import kotlin.math.abs

class GeofenceBroadcastReceiver :
    BroadcastReceiver(),
    KoinComponent {
    private val tag = "GeofenceBroadcastReceiver"

    private val geofenceRepository: GeofenceRepository by inject()
    private val mealRepository: MealRepository by inject()
    private val statisticsRepository: StatisticsRepository by inject()
    private val userRepository: UserRepository by inject()
    private val settingsRepository: SettingsRepository by inject()

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        Log.i(tag, "Activated")
        val notificationManager =
            context?.let {
                ContextCompat.getSystemService(
                    it,
                    NotificationManager::class.java,
                )
            } as NotificationManager

        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) } ?: return

        if (geofencingEvent.hasError()) {
            val errorMassage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(tag, errorMassage)
            return
        }

        val alertString =
            "Geofence Alert :" +
                " Trigger ${geofencingEvent.triggeringGeofences}" +
                " Transition ${geofencingEvent.geofenceTransition}"
        Log.d(tag, alertString)

        val userId = userRepository.getUserId()

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.i(tag, "GEOFENCE ENTERED")
                val currentTime = timeFormat.format(Date())
                geofenceRepository.saveEnterTime(currentTime)

                CoroutineScope(Dispatchers.IO).launch {
                    geofenceRepository.sendEnterEvent(
                        dateFormat.format(Date()),
                        currentTime,
                    )
                }
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.i(tag, "GEOFENCE EXITED")
                CoroutineScope(Dispatchers.IO).launch {
                    val settings = settingsRepository.getSettings().first()
                    val timeExited = timeFormat.format(Date())
                    val timeEntered = geofenceRepository.getEnterTime()

                    val enteredsplit = timeEntered.split(":").toTypedArray()
                    val exitedsplit = timeExited.split(":").toTypedArray()

                    val alldiff: Int = calculateTimeDifference(enteredsplit, exitedsplit)

                    val correctmeal = calculateCorrectMeal(timeEntered, timeExited)

                    if (alldiff > settings.eatingSpeedThreshold && settings.autoDeduct && correctmeal != null) {
                        automaticallyDeductToken(timeEntered, timeExited, correctmeal)
                        notificationManager.sendAutomaticDeductNotification(context, alldiff, correctmeal)

                        if (userId.isNotEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                geofenceRepository.sendExitEvent(
                                    timeExited,
                                    findEngMeal(correctmeal.name),
                                )
                            }
                        }
                    } else if (correctmeal != null) {
                        notificationManager.sendAteMealNotification(
                            context,
                            timeEntered,
                            timeExited,
                            correctmeal,
                        )
                    }
                }
            }

            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                val currentTime = timeFormat.format(Date())
                CoroutineScope(Dispatchers.IO).launch {
                    geofenceRepository.sendEnterEvent(
                        dateFormat.format(Date()),
                        currentTime,
                    )
                }
            }
        }
    }

    private fun automaticallyDeductToken(
        timeEntered: String,
        timeExited: String,
        mealdata: MealData?,
    ) {
        if (mealdata != null) {
            val mealIndex = findMealIndex(mealdata)

            val statisticsMeal =
                EatingStatisticsData(
                    LocalDate.now(),
                    timeEntered,
                    timeExited,
                    mealdata.name,
                )

            CoroutineScope(Dispatchers.IO).launch {
                statisticsRepository.appendMealEvent(statisticsMeal)

                val mealPrefs = mealRepository.getMealCounts().first()
                val currentMeals =
                    when (mealIndex) {
                        0 -> mealPrefs.breakfast
                        1 -> mealPrefs.lunch
                        2 -> mealPrefs.dinner
                        else -> 0
                    }

                if (currentMeals > 0) {
                    val newMeals = currentMeals - 1
                    val newPrefs =
                        mealPrefs.copy(
                            breakfast = if (mealIndex == 0) newMeals else mealPrefs.breakfast,
                            lunch = if (mealIndex == 1) newMeals else mealPrefs.lunch,
                            dinner = if (mealIndex == 2) newMeals else mealPrefs.dinner,
                        )
                    mealRepository.saveMealCounts(newPrefs)
                }
            }
        }
    }
}

/**
 * Returns the meals english name
 */
fun findEngMeal(type: Uitext): String {
    for ((i, m) in MealSampleBudget.withIndex()) {
        if (m.name == type) {
            return CalendarData.mealNames[i]
        }
    }
    return ""
}

fun calculateCorrectMeal(
    timeEntered: String,
    timeExited: String,
): MealData? {
    val enteredsplit = timeEntered.split(":").toTypedArray()
    val exitedsplit = timeExited.split(":").toTypedArray()

    for (mealdata in MealSampleBudget) {
        if (mealdata.start_hour <= (enteredsplit[0].toInt()) && mealdata.end_hour >= (exitedsplit[0].toInt())) {
            return mealdata
        }
    }
    return null
}

fun calculateTimeDifference(
    enteredsplit: Array<String>,
    exitedsplit: Array<String>,
): Int {
    val enteredMinutes = enteredsplit[0].toInt() * 60 + enteredsplit[1].toInt()
    val exitedMinutes = exitedsplit[0].toInt() * 60 + exitedsplit[1].toInt()

    return abs(exitedMinutes - enteredMinutes)
}

fun findMealIndex(mealdata: MealData): Int {
    var found = false
    var mealIndex = 0
    for (m in MealSampleBudget) {
        if (mealdata == m) {
            found = true
            break
        }
        mealIndex++
    }

    if (!found) {
        mealIndex = 0
        for (m in MealSampleSelfFinancing) {
            if (mealdata == m) {
                break
            }
            mealIndex++
        }
    }
    return mealIndex
}
