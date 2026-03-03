package com.pappt04.menzans.repository

import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepository(
    private val apiService: MenzaApiService,
) {
    suspend fun getTodayMenu(): Result<DayMenu> =
        try {
            val response = withContext(Dispatchers.IO) { apiService.getTodayMenu() }
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No menu available for today"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getWeekMenu(): Result<Map<String, DayMenu>> =
        try {
            val response = withContext(Dispatchers.IO) { apiService.getWeekMenu() }
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("No menu available"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
