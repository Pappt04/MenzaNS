package com.pappt04.menzans.repository

import android.content.Context
import com.pappt04.menzans.models.EnterEventString
import com.pappt04.menzans.models.ExitEventString
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeofenceRepository(
    private val apiService: MenzaApiService,
    private val userRepository: UserRepository,
    private val context: Context
) {
    private val prefs = context.getSharedPreferences("geofence_prefs", Context.MODE_PRIVATE)

    fun saveEnterTime(time: String) {
        prefs.edit().putString("enter_time", time).apply()
    }

    fun getEnterTime(): String {
        return prefs.getString("enter_time", "") ?: ""
    }

    suspend fun sendEnterEvent(date: String, time: String) {
        val userId = userRepository.getUserId()
        if (userId.isEmpty() || date.isEmpty() || time.isEmpty()) return

        val eventData = EnterEventString(userId, date, time)
        try {
            withContext(Dispatchers.IO) {
                apiService.enterMenza(eventData)
            }
        } catch (_: Exception) {
        }
    }

    suspend fun sendExitEvent(time: String, token: String) {
        val userId = userRepository.getUserId()
        if (userId.isEmpty() || time.isEmpty() || token.isEmpty()) return

        val eventData = ExitEventString(userId, time, token)
        try {
            withContext(Dispatchers.IO) {
                apiService.exitMenza(eventData)
            }
        } catch (_: Exception) {
        }
    }
}
