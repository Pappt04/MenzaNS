package com.pappt04.menzans.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import com.pappt04.menzans.R
import com.pappt04.menzans.data.api.getWaitTime
import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object MenzaCardWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    fun Content() {
        val context = LocalContext.current
        val data = runBlocking { MealDataStoreManager(context).getFromDataStore().first() }
        val waittime = remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            getWaitTime(context) { wt ->
                if (wt != null) {
                    waittime.intValue = wt.waittime.toInt()
                }
            }
        }

//        Box(modifier = GlanceModifier.fillMaxSize().background(MaterialTheme.colorScheme.background).clickable {
//            actionRunCallback<UpdateActionCallback>()
//        }) {
//            Column{
//                Text(
//                    text = "${data.breakfast} ${data.lunch} ${data.dinner} ${data.balance}",
//                    modifier = GlanceModifier.padding(16.dp)
//                )
//                Button(text="refresh", onClick = actionRunCallback(UpdateActionCallback::class.java))
//            }
//        }
        Scaffold(
            titleBar = {
                Text(
                    "Wait time: ${waittime.intValue} min",
                    modifier = GlanceModifier.padding(10.dp)
                )
            },
            backgroundColor = ColorProvider(R.color.white)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.fillMaxSize()
            ) {
                MealPart("Breakfast", data.breakfast)
                MealPart("Lunch", data.lunch)
                MealPart("Dinner", data.dinner)
                MealPart("Balance", data.balance)

                Button(
                    "Refresh",
                    onClick = actionRunCallback(UpdateActionCallback::class.java),
                    modifier = GlanceModifier.padding(4.dp).fillMaxWidth()
                )
            }
        }
    }

    @Composable
    fun MealPart(name: String, rem: Int) {
        Text("$name: $rem", modifier = GlanceModifier.padding(6.dp))
    }
}

class CardWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = MenzaCardWidget
}

class UpdateActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {

        updateAppWidgetState(context, glanceId) { prefs ->
            onRun(context, glanceId)
        }

        MenzaCardWidget.update(context, glanceId)
    }

    suspend fun onRun(context: Context, glanceId: GlanceId) {
        // Get a reference to the DataStore.
        val dataStore = MealDataStoreManager(context)

        // Get the current value, or a default if it doesn't exist.
        val currentValueFlow = dataStore.getFromDataStore()
        val currentValue = currentValueFlow.first()
    }
}
