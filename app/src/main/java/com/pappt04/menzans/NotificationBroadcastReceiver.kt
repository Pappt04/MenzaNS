package com.pappt04.menzans

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.DummyData.engmeals
import com.pappt04.menzans.DummyData.engmonths
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

        if (message != null && entered != null && exited != null && enteredsplit != null && exitedsplit != null && context != null && meal != null) {

            for (m in MealSample) {
                if (meal == m.name.asString(context))
                    break
                mealIndex++
            }

            when (message) {
                DummyData.ACTION_DISMISS -> usedMeals = 0
                DummyData.ACTION_CONFIRM -> usedMeals = 1
                DummyData.ACTION_TWICE -> usedMeals = 2
            }

            //Maybe it should just check entered time
            val currentTokens = context.let {
                var fdao= FileDAO(it,DummyData.FileNames[mealIndex])
                fdao.getDAOData()
            }
            context.let {
                if (currentTokens.toInt() >= usedMeals) {
                    var fdao= FileDAO(it,DummyData.FileNames[mealIndex])
                    fdao.saveToFile(currentTokens.toInt()-usedMeals,true)

                    if(UserID.userid != "")
                        sendExitEvent(fdao.getDAOData(),exited, engmeals[mealIndex],context )

                }
            }

        }
    }
}