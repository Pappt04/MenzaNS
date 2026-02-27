package com.pappt04.menzans.views.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import kotlinx.coroutines.flow.first

// ── Tokens Widget ───────────────────────────────────────────────────────────

object TokensWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val data = MealDataStoreManager(context).getFromDataStore().first()
        provideContent {
            GlanceTheme {
                Scaffold(
                    titleBar = {
                        Text(
                            "Tokens",
                            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    },
                    backgroundColor = GlanceTheme.colors.widgetBackground,
                ) {
                    Column(
                        modifier = GlanceModifier.fillMaxSize().padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TokenRow("Breakfast", data.breakfast)
                        Spacer(GlanceModifier.height(6.dp))
                        TokenRow("Lunch", data.lunch)
                        Spacer(GlanceModifier.height(6.dp))
                        TokenRow("Dinner", data.dinner)
                        Spacer(GlanceModifier.height(10.dp))
                        TokenRow("Balance", data.balance, bold = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun TokenRow(label: String, count: Int, bold: Boolean = false) {
    Row(
        modifier = GlanceModifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 14.sp,
                fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            ),
        )
        Spacer(GlanceModifier.defaultWeight())
        Text(
            "$count",
            style = TextStyle(
                color = GlanceTheme.colors.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

class TokensWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = TokensWidget
}

// ── Wait Time Widget ────────────────────────────────────────────────────────

object WaitTimeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        var minutes = -1
        var lineLength = ""
        try {
            val api = com.pappt04.menzans.service.RetrofitClient.apiService
            val response = api.getWaitTime()
            if (response.isSuccessful) {
                response.body()?.let {
                    minutes = it.waittime?.toIntOrNull() ?: -1
                    lineLength = it.linelength ?: ""
                }
            }
        } catch (_: Exception) { }

        provideContent {
            GlanceTheme {
                Scaffold(
                    titleBar = {
                        Text(
                            "Canteen",
                            modifier = GlanceModifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                    },
                    backgroundColor = GlanceTheme.colors.widgetBackground,
                ) {
                    Column(
                        modifier = GlanceModifier.fillMaxSize().padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (minutes >= 0) {
                            Text(
                                "$minutes",
                                style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                            Text(
                                "min wait",
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontSize = 14.sp,
                                ),
                            )
                            if (lineLength.isNotEmpty()) {
                                Spacer(GlanceModifier.height(6.dp))
                                Text(
                                    "$lineLength people in line",
                                    style = TextStyle(
                                        color = GlanceTheme.colors.secondary,
                                        fontSize = 12.sp,
                                    ),
                                )
                            }
                        } else {
                            Text(
                                "—",
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontSize = 32.sp,
                                ),
                            )
                            Text(
                                "No data",
                                style = TextStyle(
                                    color = GlanceTheme.colors.secondary,
                                    fontSize = 14.sp,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

class WaitTimeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = WaitTimeWidget
}

