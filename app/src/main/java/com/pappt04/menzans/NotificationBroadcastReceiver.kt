package com.pappt04.menzans

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pappt04.menzans.DummyData.MealSample
import java.time.format.DateTimeFormatter

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ACTION")
        val entered = intent?.getStringExtra("START_TIME")
        val exited = intent?.getStringExtra("END_TIME")
        val meal = intent?.getStringExtra("MEAL")

        var usedMeals = 0
        var mealIndex = 0

        val enteredsplit = entered?.split(":")?.toTypedArray()
        val exitedsplit = exited?.split(":")?.toTypedArray()

        if (message != null && entered != null && exited != null && enteredsplit != null && exitedsplit != null && context!= null && meal!= null) {

            for(m in MealSample)
            {
                if(meal == m.name.asString(context))
                    break
                mealIndex++
            }

            when (message) {
                DummyData.ACTION_DISMISS -> usedMeals = 0
                DummyData.ACTION_CONFIRM -> usedMeals = 1
                DummyData.ACTION_TWICE -> usedMeals = 2
            }

            //Maybe it should just check entered time
            val currentTokens = context?.let { readFromFile(it, DummyData.FileNames[mealIndex]) }
            context?.let {
                if (currentTokens != null && currentTokens.toInt() >= usedMeals) {
                    saveToFile(
                        it,
                        DummyData.FileNames[mealIndex],
                        currentTokens.toInt() - usedMeals,
                        true
                    )
                }
            }

        }
    }
}

fun monthStatisticsSavetoFile(context: Context, month: String, meal: EatingStatisticsData) {
    val realmonth = DummyData.engmonths[month.toInt() - 1]

    var i = 0
    var s = ""
    for (m in DummyData.MealSample) {
        if (m.name.asString(context) == meal.tokentype)
            s = DummyData.engmeals[i]
        i++
    }
    val formatter= DateTimeFormatter.ofPattern(DummyData.datetypeall.toPattern())

    val s1 = "${meal.date.format(formatter)},${meal.timeentered},${meal.timeexited},$s;\n"
    context.openFileOutput(realmonth, Context.MODE_APPEND).use {
        it.write(s1.toByteArray())
    }
}