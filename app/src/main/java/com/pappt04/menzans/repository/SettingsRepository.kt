package com.pappt04.menzans.repository

import com.pappt04.menzans.data.local.datastore.SettingsDataStoreManager
import com.pappt04.menzans.models.SettingsPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val settingsDataStoreManager: SettingsDataStoreManager
) {
    fun getSettings(): Flow<SettingsPreferences> = settingsDataStoreManager.getFromDataStore()

    suspend fun saveSettings(prefs: SettingsPreferences) {
        settingsDataStoreManager.saveToDataStore(prefs)
    }

    suspend fun clearSettings() {
        settingsDataStoreManager.clearDataStore()
    }
}
