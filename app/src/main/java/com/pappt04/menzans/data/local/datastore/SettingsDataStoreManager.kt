package com.pappt04.menzans.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pappt04.menzans.models.SettingsPreferences
import kotlinx.coroutines.flow.map

const val SETTINGS_DATASTORE = "settings_data"

val Context.settingspreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

class SettingsDataStoreManager(
    val context: Context,
) {
    companion object {
        val LANGUAGE = stringPreferencesKey("LANGUAGE")
        val DARK_THEME = booleanPreferencesKey("DARK_THEME")
        val MATERIALYOU_THEME = booleanPreferencesKey("MATERIALYOU_THEME")
        val BUDGET = booleanPreferencesKey("BUDGET")
        val BREAKFAST_TOKEN_WARNING = intPreferencesKey("BREAKFAST_TOKEN_WARNING")
        val LUNCH_TOKEN_WARNING = intPreferencesKey("LUNCH_TOKEN_WARNING")
        val DINNER_TOKEN_WARNING = intPreferencesKey("DINNER_TOKEN_WARNING")
        val USERID = stringPreferencesKey("USERID")
        val FIRST_WELCOME = booleanPreferencesKey("FIRST_WELCOME")
        val GEOFENCE_ENABLED = booleanPreferencesKey("GEOFENCE_ENABLED")
        val EATING_SPEED_THRESHOLD = intPreferencesKey("EATING_SPEED_THRESHOLD")
        val AUTO_DEDUCT = booleanPreferencesKey("AUTO_DEDUCT")
        val BREAKFAST_NOTIFY_THRESHOLD = intPreferencesKey("BREAKFAST_NOTIFY_THRESHOLD")
        val LUNCH_NOTIFY_THRESHOLD = intPreferencesKey("LUNCH_NOTIFY_THRESHOLD")
        val DINNER_NOTIFY_THRESHOLD = intPreferencesKey("DINNER_NOTIFY_THRESHOLD")
        val GEOFENCE_RADIUS = floatPreferencesKey("GEOFENCE_RADIUS")
    }

    suspend fun saveToDataStore(settingsData: SettingsPreferences) {
        Log.d("SETTINGS_DATASTORE_MANAGER", "Successfully saved to Datastore")
        context.settingspreferenceDataStore.edit {
            it[LANGUAGE] = settingsData.language
            it[DARK_THEME] = settingsData.darktheme
            it[MATERIALYOU_THEME] = settingsData.materialyoutheme
            it[BUDGET] = settingsData.budget
            it[BREAKFAST_TOKEN_WARNING] = settingsData.breakfastTokenWarning
            it[LUNCH_TOKEN_WARNING] = settingsData.lunchTokenWarning
            it[DINNER_TOKEN_WARNING] = settingsData.dinnerTokenWarning
            it[USERID] = settingsData.userID
            it[FIRST_WELCOME] = settingsData.firstWelcome
            it[GEOFENCE_ENABLED] = settingsData.geofenceEnabled
            it[EATING_SPEED_THRESHOLD] = settingsData.eatingSpeedThreshold
            it[AUTO_DEDUCT] = settingsData.autoDeduct
            it[BREAKFAST_NOTIFY_THRESHOLD] = settingsData.breakfastNotifyThreshold
            it[LUNCH_NOTIFY_THRESHOLD] = settingsData.lunchNotifyThreshold
            it[DINNER_NOTIFY_THRESHOLD] = settingsData.dinnerNotifyThreshold
            it[GEOFENCE_RADIUS] = settingsData.geofenceRadius
        }
    }

    fun getFromDataStore() =
        context.settingspreferenceDataStore.data.map {
            Log.d("SETTINGS_DATASTORE_MANAGER", "Successfully loaded from Datastore")
            SettingsPreferences(
                language = it[LANGUAGE] ?: "",
                darktheme = it[DARK_THEME] ?: false,
                materialyoutheme = it[MATERIALYOU_THEME] ?: false,
                budget = it[BUDGET] ?: false,
                breakfastTokenWarning = it[BREAKFAST_TOKEN_WARNING] ?: 2,
                lunchTokenWarning = it[LUNCH_TOKEN_WARNING] ?: 2,
                dinnerTokenWarning = it[DINNER_TOKEN_WARNING] ?: 2,
                userID = it[USERID] ?: "",
                firstWelcome = it[FIRST_WELCOME] ?: true,
                geofenceEnabled = it[GEOFENCE_ENABLED] ?: true,
                eatingSpeedThreshold = it[EATING_SPEED_THRESHOLD] ?: 15,
                autoDeduct = it[AUTO_DEDUCT] ?: true,
                breakfastNotifyThreshold = it[BREAKFAST_NOTIFY_THRESHOLD] ?: 70,
                lunchNotifyThreshold = it[LUNCH_NOTIFY_THRESHOLD] ?: 70,
                dinnerNotifyThreshold = it[DINNER_NOTIFY_THRESHOLD] ?: 70,
                geofenceRadius = it[GEOFENCE_RADIUS] ?: 25f,
            )
        }

    suspend fun clearDataStore() =
        context.settingspreferenceDataStore.edit {
            Log.d("SETTINGS_DATASTORE_MANAGER", "Successfully cleared Datastore")
            it.clear()
        }
}
