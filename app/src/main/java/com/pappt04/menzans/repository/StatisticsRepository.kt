package com.pappt04.menzans.repository

import android.content.Context
import com.pappt04.menzans.data.local.StatisticsFileDAO
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatisticsRepository(
    private val apiService: MenzaApiService,
    private val userRepository: UserRepository,
    private val context: Context
) {
    fun getStatisticsForMonth(month: String): MutableList<EatingStatisticsData> {
        val dao = StatisticsFileDAO(context, month)
        return dao.getStatisticsData()
    }

    suspend fun addMealEvent(meal: MealEventString, month: String, mealData: EatingStatisticsData) {
        val dao = StatisticsFileDAO(context, month)
        dao.appendToStatisticsFile(mealData)

        try {
            withContext(Dispatchers.IO) {
                apiService.addMeal(meal)
            }
        } catch (_: Exception) {
        }
    }

    suspend fun removeMealEvent(
        meal: MealEventString,
        data: EatingStatisticsData,
        month: String,
        allData: List<EatingStatisticsData>
    ) {
        val dao = StatisticsFileDAO(context, month)
        val mutableData = allData.toMutableList()
        mutableData.remove(data)
        dao.saveStatisticsToFile(mutableData)

        try {
            withContext(Dispatchers.IO) {
                apiService.removeMeal(meal)
            }
        } catch (_: Exception) {
        }
    }

    fun appendToStatisticsFile(meal: EatingStatisticsData, month: String) {
        val dao = StatisticsFileDAO(context, month)
        dao.appendToStatisticsFile(meal)
    }
}
