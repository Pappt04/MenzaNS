package com.pappt04.menzans

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.pappt04.menzans.DummyData.MealSample
import java.time.Month
import java.util.Locale

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ACTION")
        val entered = intent?.getStringExtra("START_TIME")
        val exited = intent?.getStringExtra("END_TIME")

        var usedMeals = 0
        var correctMeal=0

        val enteredsplit = entered?.split(":")?.toTypedArray()
        val exitedsplit = exited?.split(":")?.toTypedArray()

        if (message != null && entered != null && exited != null && enteredsplit!= null && exitedsplit != null) {
            when (message) {
                DummyData.ACTION_DISMISS -> usedMeals = 0
                DummyData.ACTION_CONFIRM -> usedMeals = 1
                DummyData.ACTION_TWICE -> usedMeals = 2
            }
            for (mealdata in MealSample) {
                if(mealdata.start_hour <= (enteredsplit?.get(0)?.toInt() ?: 0) && mealdata.end_hour >= (exitedsplit?.get(0)?.toInt() ?: 0) && calculateTimeDifference(enteredsplit,exitedsplit)> DummyData.DWELL_TRESHOLD)
                {
                    //Maybe it should just check entered time
                    val currentTokens= context?.let { readFromFile(it,DummyData.FileNames[correctMeal]) }
                    context?.let {
                        if (currentTokens != null && currentTokens.toInt() >= usedMeals) {
                            saveToFile(it,DummyData.FileNames[correctMeal],currentTokens.toInt()-usedMeals,true)
                        }
                    }
                }
                correctMeal++
            }
        }
    }
}

fun monthStatisticsSavetoFile(context: Context, month: String, meal: EatingStatisticsData)
{
    val realmonth= DummyData.engmonths[month.toInt()-1]
    val s1= "${meal.date};${meal.timeentered};${meal.timeexited};${meal.tokentype.name.asString(context)}"
    context.openFileOutput(realmonth, Context.MODE_APPEND).use {
        it.write(s1.toByteArray())
    }
}

fun translate(context: Context,locale: Locale?, resId: Int): String {
    val config: Configuration = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config).getText(resId).toString()
}