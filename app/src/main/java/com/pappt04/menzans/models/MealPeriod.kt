package com.pappt04.menzans.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.Calendar

internal enum class MealPeriod(
    val label: String,
    val icon: ImageVector,
    val startH: Int,
    val startM: Int,
    val endH: Int,
    val endM: Int,
) {
    BREAKFAST("Doručak", Icons.Default.Coffee, 7, 0, 9, 30),
    LUNCH("Ručak", Icons.Filled.Restaurant, 11, 0, 15, 0),
    DINNER("Večera", Icons.Outlined.Fastfood, 17, 0, 20, 30),
    ;

    fun minuteStart() = startH * 60 + startM

    fun minuteEnd() = endH * 60 + endM
}

internal fun currentOrNextPeriod(): MealPeriod {
    val cal = Calendar.getInstance()
    val now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    return MealPeriod.entries.firstOrNull { now < it.minuteEnd() } ?: MealPeriod.BREAKFAST
}

internal fun MealPeriod.itemsFrom(menu: DayMenu): List<String> =
    when (this) {
        MealPeriod.BREAKFAST -> menu.breakfast
        MealPeriod.LUNCH -> menu.lunch
        MealPeriod.DINNER -> menu.dinner
    }

internal fun MealPeriod.isActive(): Boolean {
    val cal = Calendar.getInstance()
    val now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    return now in minuteStart() until minuteEnd()
}
