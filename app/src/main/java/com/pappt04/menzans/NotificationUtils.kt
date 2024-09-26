package com.pappt04.menzans

import android.app.NotificationChannel
import android.app.NotificationManager
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

fun NotificationManager.sendAteMealNotification(context: Context, timeEntered:String, timeExited: String) {
    //TODO
    val notification= NotificationCompat.Builder(context,DummyData.CHANNEL_IDs[0])
        .setContentTitle("Record your consumed meals")
        .setContentText("We think you were at Menza from $timeEntered to $timeExited")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
    notify(DummyData.NOTIFICATION_IDs.first,notification.build())
}

fun NotificationManager.sendDummyText(context: Context,text: String)
{
    val notification = NotificationCompat.Builder(context, DummyData.CHANNEL_IDs[1])
        .setContentTitle("Menza TEST Message")
        .setContentText(text)
        .setSmallIcon(R.drawable.ic_launcher_background)

    notify(33,notification.build())
}
