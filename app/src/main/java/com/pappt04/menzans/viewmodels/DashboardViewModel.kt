package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.data.consts.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.models.MealData
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.models.UiState
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.SettingsRepository
import com.pappt04.menzans.repository.WaitTimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val mealRepository: MealRepository,
    private val waitTimeRepository: WaitTimeRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val meals: StateFlow<List<MealData>> = settingsRepository.getSettings()
        .map { if (it.budget) MealSampleBudget else MealSampleSelfFinancing }
        .stateIn(viewModelScope, SharingStarted.Lazily, MealSampleSelfFinancing)

    val mealCounts: StateFlow<MealPreferences> = mealRepository.getMealCounts()
        .stateIn(viewModelScope, SharingStarted.Lazily, MealPreferences())

    private val _graphState = MutableStateFlow<UiState>(UiState.Empty)
    val graphState: StateFlow<UiState> = _graphState.asStateFlow()

    fun saveMealCounts(prefs: MealPreferences) {
        viewModelScope.launch { mealRepository.saveMealCounts(prefs) }
    }

    fun fetchGraphData() {
        viewModelScope.launch {
            _graphState.value = UiState.Loading
            val result = waitTimeRepository.getLineGraph()
            if (result.isSuccess) {
                _graphState.value = UiState.Success(result.getOrNull() ?: emptyMap())
            } else {
                _graphState.value = UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}
