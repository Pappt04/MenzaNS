package com.pappt04.menzans

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pappt04.menzans.DummyData.MealSample

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ACTION")
        val entered = intent?.getStringExtra("START_TIME")
        val exited = intent?.getStringExtra("END_TIME")

        var usedMeals = 0
        var correctMeal=0

        val enteredsplit = entered?.split(":")?.toTypedArray()
        val exitedsplit = exited?.split(":")?.toTypedArray()

        if (message != null && entered != null && exited != null) {
            when (message) {
                DummyData.ACTION_DISMISS -> usedMeals = 0
                DummyData.ACTION_CONFIRM -> usedMeals = 1
                DummyData.ACTION_TWICE -> usedMeals = 2
            }
            for (mealdata in MealSample) {
                if(mealdata.start_hour <= (enteredsplit?.get(0)?.toInt() ?: 0) && mealdata.end_hour >= (exitedsplit?.get(0)?.toInt() ?: 0))
                {
                    //Maybe it should just check entered time
                    val currentTokens= context?.let { readFromFile(it,DummyData.FileNames[correctMeal]) }
                    context?.let {
                        if (currentTokens != null) {
                            saveToFile(it,DummyData.FileNames[correctMeal],currentTokens.toInt()-usedMeals)
                        }
                    }
                }
                correctMeal++
            }
        }
    }
}