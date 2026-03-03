package com.pappt04.menzans.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.ui.graphics.vector.ImageVector
import com.pappt04.menzans.R
import java.util.Calendar
import java.util.TimeZone

// All time comparisons use Belgrade timezone so period detection is correct
// for users whose device is set to a different timezone.
private val BELGRADE_TZ = TimeZone.getTimeZone("Europe/Belgrade")

private fun nowMinutesBelgrade(): Int {
    val cal = Calendar.getInstance(BELGRADE_TZ)
    return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
}

internal enum class MealPeriod(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val startH: Int,
    val startM: Int,
    val endH: Int,
    val endM: Int,
) {
    BREAKFAST(R.string.breakfast, Icons.Default.Coffee, 7, 0, 9, 30),
    LUNCH(R.string.lunch, Icons.Filled.Restaurant, 11, 0, 15, 0),
    DINNER(R.string.dinner, Icons.Outlined.Fastfood, 17, 0, 20, 30),
    ;

    fun minuteStart() = startH * 60 + startM

    fun minuteEnd() = endH * 60 + endM
}

internal fun currentOrNextPeriod(): MealPeriod {
    val now = nowMinutesBelgrade()
    // Return the currently active period; if between meals, return the next one;
    // if past dinner, wrap to breakfast (next day).
    return MealPeriod.entries.firstOrNull { now in it.minuteStart() until it.minuteEnd() }
        ?: MealPeriod.entries.firstOrNull { now < it.minuteStart() }
        ?: MealPeriod.BREAKFAST
}

internal fun MealPeriod.itemsFrom(menu: DayMenu): List<String> =
    when (this) {
        MealPeriod.BREAKFAST -> menu.breakfast ?: emptyList()
        MealPeriod.LUNCH -> menu.lunch ?: emptyList()
        MealPeriod.DINNER -> menu.dinner ?: emptyList()
    }

internal fun MealPeriod.isActive(): Boolean {
    val now = nowMinutesBelgrade()
    return now in minuteStart() until minuteEnd()
}
