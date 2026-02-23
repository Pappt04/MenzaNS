package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.stateIn
import com.pappt04.menzans.models.SettingsPreferences
import com.pappt04.menzans.repository.SettingsRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val settings: StateFlow<SettingsPreferences> = settingsRepository.getSettings().stateIn(
        viewModelScope,
        kotlinx.coroutines.flow.SharingStarted.Lazily,
        SettingsPreferences()
    )

    fun updateSettings(prefs: SettingsPreferences) {
        viewModelScope.launch {
            settingsRepository.saveSettings(prefs)
        }
    }
}
