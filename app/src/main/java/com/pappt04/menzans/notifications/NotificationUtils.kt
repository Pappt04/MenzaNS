package com.pappt04.menzans.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.pappt04.menzans.R
import com.pappt04.menzans.data.consts.MealSample
import com.pappt04.menzans.data.consts.NotificationConstants
import com.pappt04.menzans.models.MealData
import com.pappt04.menzans.views.MainActivity

fun createChannel(context: Context) {
    for (channel in NotificationConstants.CHANNEL_IDs) {
        val notificationChannel =
            NotificationChannel(
                channel,
                channel,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                setShowBadge(false)
            }
        notificationChannel.enableVibration(true)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendAteMealNotification(
    context: Context,
    timeEntered: String,
    timeExited: String,
    mealData: MealData,
) {
    val flag =
        PendingIntent.FLAG_IMMUTABLE

    val tapIntent =
        Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    val tapPendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_IMMUTABLE)

    val dismissIntent =
        Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("ACTION", NotificationConstants.ACTION_DISMISS)
            putExtra("START_TIME", timeEntered)
            putExtra("END_TIME", timeExited)
            putExtra("MEAL", mealData.name.asString(context))
        }
    val dismissPendingIntent = PendingIntent.getBroadcast(context, 100, dismissIntent, flag)

    val confrimIntent =
        Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("ACTION", NotificationConstants.ACTION_CONFIRM)
            putExtra("START_TIME", timeEntered)
            putExtra("END_TIME", timeExited)
            putExtra("MEAL", mealData.name.asString(context))
        }
    val confirmPendingIntent = PendingIntent.getBroadcast(context, 200, confrimIntent, flag)

    val doubleIntent =
        Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("ACTION", NotificationConstants.ACTION_TWICE)
            putExtra("START_TIME", timeEntered)
            putExtra("END_TIME", timeExited)
            putExtra("MEAL", mealData.name.asString(context))
        }
    val doublePendingIntent = PendingIntent.getBroadcast(context, 300, doubleIntent, flag)

    val notification =
        NotificationCompat
            .Builder(context, NotificationConstants.CHANNEL_IDs[0])
            .setContentTitle(context.getString(R.string.record_your_consumed_meals_notification))
            .setContentText(
                context.getString(
                    R.string.we_think_you_were_at_menza_from_to_notification,
                    timeEntered,
                    timeExited,
                ),
            ).setSmallIcon(R.mipmap.ic_launcher_monochrome_foreground)
            .setContentIntent(tapPendingIntent)
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.no), dismissPendingIntent)
            .addAction(0, context.getString(R.string.yes), confirmPendingIntent)
            .addAction(0, context.getString(R.string.twice), doublePendingIntent)
    notify(NotificationConstants.NOTIFICATION_IDs.first, notification.build())
}

fun NotificationManager.sendAutomaticDeductNotification(
    context: Context,
    minutes: Int,
    mealData: MealData,
) {
    val notification =
        NotificationCompat
            .Builder(context, NotificationConstants.CHANNEL_IDs[0])
            .setContentTitle(context.getString(R.string.we_automatically_deducted_one_meal_token_for_you_notification))
            .setContentText(
                context.getString(
                    R.string.we_think_you_were_at_menza_for_there_is_a_big_chance_you_ate_notification,
                    minutes.toString(),
                    mealData.name.asString(context),
                ),
            ).setSmallIcon(R.mipmap.ic_launcher_monochrome_foreground)
            .setAutoCancel(true)

    notify(100, notification.build())
}

fun NotificationManager.sendTopUpReminder(
    context: Context,
    index: Int,
    remaining: Int,
) {
    var s1 = ""
    try {
        s1 = MealSample.MealSampleBudget[index%3].name.asString(context)
    } catch (_: Exception) {
    }

    val notification =
        NotificationCompat
            .Builder(context, NotificationConstants.CHANNEL_IDs[1])
            .setContentTitle(context.getString(R.string.you_should_top_up_your_card))
            .setContentText(
                context.getString(
                    R.string.reminder_that_you_only_have_tokens_on_your_card,
                    remaining.toString(),
                    s1,
                ),
            ).setSmallIcon(R.mipmap.ic_launcher_monochrome_foreground)
            .setAutoCancel(true)

    notify(101, notification.build())
}

fun NotificationManager.sendDummyText(
    context: Context,
    text: String,
) {
    val notification =
        NotificationCompat
            .Builder(context, NotificationConstants.CHANNEL_IDs[1])
            .setContentTitle("Menza TEST Message")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_background)

    notify(33, notification.build())
}
