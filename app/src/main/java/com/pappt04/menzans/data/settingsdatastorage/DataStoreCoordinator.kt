package com.pappt04.menzans.data.settingsdatastorage

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull

suspend fun SettingsDataCoordinator.getBooleanDataStore(key: Preferences.Key<Boolean>): Boolean {
    val context = this.context ?: return financingDefault
    return context.dataStore.data.firstOrNull()?.get(key) ?: financingDefault
}

suspend fun SettingsDataCoordinator.setBooleanDataStore(value: Boolean, key: Preferences.Key<Boolean>) {
    val context = this.context ?: return
    context.dataStore.edit { preferences ->
        preferences[key] = value
    }
}