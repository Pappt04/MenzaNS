package com.pappt04.menzans.repository

import android.content.Context
import com.pappt04.menzans.data.local.datastore.SettingsDataStoreManager
import com.pappt04.menzans.models.SettingsPreferences
import com.pappt04.menzans.models.UserIDString
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserRepository(
    private val settingsDataStoreManager: SettingsDataStoreManager,
    private val apiService: MenzaApiService,
    private val context: Context
) {
    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    suspend fun loadOrRegisterUser() {
        val settings = settingsDataStoreManager.getFromDataStore().first()
        var id = settings.userID

        if (id.isEmpty()) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.registerUser()
                }
                if (response.isSuccessful) {
                    id = response.body()?.userid ?: ""
                }
            } catch (_: Exception) {
            }
        }

        if (id.isNotEmpty()) {
            _userId.value = id
            val currentSettings = settingsDataStoreManager.getFromDataStore().first()
            settingsDataStoreManager.saveToDataStore(currentSettings.copy(userID = id))
        }
    }

    suspend fun deleteUser() {
        val id = _userId.value
        if (id.isEmpty()) return

        try {
            withContext(Dispatchers.IO) {
                apiService.deleteUser(UserIDString(id))
            }
        } catch (_: Exception) {
        }
    }

    fun getUserId(): String = _userId.value

    fun getSettings(): Flow<SettingsPreferences> = settingsDataStoreManager.getFromDataStore()

    suspend fun saveSettings(settings: SettingsPreferences) {
        settingsDataStoreManager.saveToDataStore(settings)
    }
}
