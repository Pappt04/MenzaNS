package com.pappt04.menzans.data.consts

import android.annotation.SuppressLint
import com.pappt04.menzans.R
import com.pappt04.menzans.models.Uitext
import java.text.SimpleDateFormat

object CalendarData {
    @SuppressLint("SimpleDateFormat")
    val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    val timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")

    @SuppressLint("SimpleDateFormat")
    val monthFormat: SimpleDateFormat = SimpleDateFormat("M")

    val weekDays =
        listOf(
            Uitext.StringResource(R.string.monday),
            Uitext.StringResource(R.string.tuesday),
            Uitext.StringResource(R.string.wednesday),
            Uitext.StringResource(R.string.thursday),
            Uitext.StringResource(R.string.friday),
            Uitext.StringResource(R.string.saturday),
            Uitext.StringResource(R.string.sunday),
        )

    val monthNames =
        listOf(
            "january",
            "february",
            "march",
            "april",
            "may",
            "june",
            "july",
            "august",
            "september",
            "october",
            "november",
            "december",
        )

    val mealNames =
        listOf(
            "Breakfast",
            "Lunch",
            "Dinner",
        )

    fun mealNameToRes(name: String): Int =
        when (name) {
            mealNames[0] -> R.string.breakfast
            mealNames[1] -> R.string.lunch
            mealNames[2] -> R.string.dinner
            else -> R.string.info
        }
}
