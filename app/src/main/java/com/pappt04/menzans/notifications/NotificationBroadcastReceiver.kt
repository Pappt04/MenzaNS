package com.pappt04.menzans.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pappt04.menzans.data.consts.DummyData
import com.pappt04.menzans.data.consts.DummyData.engmeals
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.repository.GeofenceRepository
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val userRepository: UserRepository by inject()
    private val mealRepository: MealRepository by inject()
    private val geofenceRepository: GeofenceRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ACTION")
        val entered = intent?.getStringExtra("START_TIME")
        val exited = intent?.getStringExtra("END_TIME")
        val meal = intent?.getStringExtra("MEAL")

        var usedMeals = 0
        var mealIndex = 0

        if (message != null && entered != null && exited != null && context != null && meal != null) {
            for (m in MealSampleBudget) {
                if (meal == m.name.asString(context))
                    break
                mealIndex++
            }

            when (message) {
                DummyData.ACTION_DISMISS -> usedMeals = 0
                DummyData.ACTION_CONFIRM -> usedMeals = 1
                DummyData.ACTION_TWICE -> usedMeals = 2
            }

            CoroutineScope(Dispatchers.IO).launch {
                val userId = userRepository.getUserId()
                if (userId.isNotEmpty()) {
                    val mealPrefs = mealRepository.getMealCounts().first()
                    val currentMeals = when (mealIndex) {
                        0 -> mealPrefs.breakfast
                        1 -> mealPrefs.lunch
                        2 -> mealPrefs.dinner
                        else -> 0
                    }

                    if (currentMeals >= usedMeals) {
                        val newMeals = currentMeals - usedMeals
                        val newPrefs = mealPrefs.copy(
                            breakfast = if (mealIndex == 0) newMeals else mealPrefs.breakfast,
                            lunch = if (mealIndex == 1) newMeals else mealPrefs.lunch,
                            dinner = if (mealIndex == 2) newMeals else mealPrefs.dinner
                        )
                        mealRepository.saveMealCounts(newPrefs)

                        geofenceRepository.sendExitEvent(exited, engmeals[mealIndex])
                    }
                }
            }
        }
    }
}
