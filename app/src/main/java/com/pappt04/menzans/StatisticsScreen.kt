package com.pappt04.menzans

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

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
            notificationManager.sendDummyText(context,"Test string")
        }
    )
    {
        Text("Notify")
    }
}