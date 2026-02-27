package com.pappt04.menzans.repository

import com.pappt04.menzans.models.WaitTime
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WaitTimeRepository(
    private val apiService: MenzaApiService,
) {
    suspend fun getWaitTime(): Result<WaitTime> =
        try {
            val response =
                withContext(Dispatchers.IO) {
                    apiService.getWaitTime()
                }
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get wait time"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getLineGraph(): Result<Map<String, Double>> =
        try {
            val response =
                withContext(Dispatchers.IO) {
                    apiService.getLineGraph()
                }
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get line graph"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
