package com.pappt04.menzans.data.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Fastfood
import com.pappt04.menzans.R
import com.pappt04.menzans.data.MealData
import com.pappt04.menzans.data.Uitext

object MealSample {
    var MealSampleBudget = listOf(
        MealData(Uitext.StringResource(R.string.breakfast), 56, 7, 0, 9, 30),
        MealData(Uitext.StringResource(R.string.lunch), 120, 11, 0, 15, 0),
        MealData(Uitext.StringResource(R.string.dinner), 90, 17, 0, 20, 30)
    )

    var MealSampleSelfFinancing = listOf(
        MealData(MealSampleBudget[0].name, 138, MealSampleBudget[0].start_hour, MealSampleBudget[0].start_minute, MealSampleBudget[0].end_hour, MealSampleBudget[0].end_minute),
        MealData(MealSampleBudget[1].name, 326, MealSampleBudget[1].start_hour, MealSampleBudget[1].start_minute, MealSampleBudget[1].end_hour, MealSampleBudget[1].end_minute),
        MealData(MealSampleBudget[2].name, 262, MealSampleBudget[2].start_hour, MealSampleBudget[2].start_minute, MealSampleBudget[2].end_hour, MealSampleBudget[2].end_minute)
    )

    val mealIcons = listOf(
        Icons.Default.Coffee,
        Icons.Filled.Restaurant,
        Icons.Outlined.Fastfood
    )

    fun getMeals(onBudget: Boolean): List<MealData>
    {
        return if(onBudget) MealSampleBudget else MealSampleSelfFinancing
    }
}