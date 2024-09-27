package com.pappt04.menzans

import android.app.NotificationManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun StatisticsScreen(innerpadding: PaddingValues) {
    val context = LocalContext.current
    Button(
        modifier = Modifier.padding(innerpadding),
        onClick = {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
           notificationManager.sendAteMealNotification(context,"12:30","13:09")
        }
    )
    {
        Text("Notify")
    }
}