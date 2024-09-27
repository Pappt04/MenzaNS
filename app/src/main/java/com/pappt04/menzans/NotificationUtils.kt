package com.pappt04.menzans

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        for (channel in DummyData.CHANNEL_IDs) {
            val notificationChannel = NotificationChannel(
                channel,
                channel,
                NotificationManager.IMPORTANCE_LOW
            )
                .apply {
                    setShowBadge(false)
                }
            notificationChannel.enableVibration(true)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}

fun NotificationManager.sendAteMealNotification(
    context: Context,
    timeEntered: String,
    timeExited: String
) {
    val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            PendingIntent.FLAG_MUTABLE

    val Tapintent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val TappendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, Tapintent, PendingIntent.FLAG_IMMUTABLE)

    val Dismissintent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
        putExtra("ACTION",DummyData.ACTION_DISMISS)
        putExtra("START_TIME",timeEntered)
        putExtra("END_TIME",timeExited)
    }
    val DismisspendingIntent = PendingIntent.getBroadcast(context, 100, Dismissintent, flag)

    val Confrimintent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
        putExtra("ACTION",DummyData.ACTION_CONFIRM)
        putExtra("START_TIME",timeEntered)
        putExtra("END_TIME",timeExited)
    }
    val ConfirmpendingIntent = PendingIntent.getBroadcast(context, 200, Confrimintent, flag)

    val Twiceintent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
        putExtra("ACTION",DummyData.ACTION_TWICE)
        putExtra("START_TIME",timeEntered)
        putExtra("END_TIME",timeExited)
    }
    val TwicependingIntent = PendingIntent.getBroadcast(context, 300, Twiceintent, flag)

    val notification = NotificationCompat.Builder(context, DummyData.CHANNEL_IDs[0])
        .setContentTitle("Record your consumed meals")
        .setContentText("We think you were at Menza from $timeEntered to $timeExited")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(TappendingIntent)
        .setAutoCancel(true)
        .addAction(0,"NO",DismisspendingIntent)
        .addAction(0,"YES",ConfirmpendingIntent)
        .addAction(0,"TWICE",TwicependingIntent)
    notify(DummyData.NOTIFICATION_IDs.first, notification.build())
}

fun NotificationManager.sendAutomaticDeductNotification(context: Context, minutes: Int)
{
    val notification = NotificationCompat.Builder(context, DummyData.CHANNEL_IDs[1])
        .setContentTitle("Automatically Deducted one Meal token")
        .setContentText("We think you were at Menza for $minutes there is a big chance you ate")
        .setSmallIcon(R.drawable.ic_launcher_background)

    notify(33, notification.build())
}

fun NotificationManager.sendDummyText(context: Context, text: String) {
    val notification = NotificationCompat.Builder(context, DummyData.CHANNEL_IDs[1])
        .setContentTitle("Menza TEST Message")
        .setContentText(text)
        .setSmallIcon(R.drawable.ic_launcher_background)

    notify(33, notification.build())
}
