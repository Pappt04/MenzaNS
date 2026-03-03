package com.pappt04.menzans.repository

import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class MenuRepository(
    private val apiService: MenzaApiService,
) {
    suspend fun getTodayMenu(): Result<DayMenu> =
        try {
            val response = withContext(Dispatchers.IO) { apiService.getTodayMenu() }
            // Capture body once to avoid the double-call (#8)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: SocketTimeoutException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getWeekMenu(): Result<Map<String, DayMenu>> =
        try {
            val response = withContext(Dispatchers.IO) { apiService.getWeekMenu() }
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: SocketTimeoutException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
}
