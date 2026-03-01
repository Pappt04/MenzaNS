package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.data.consts.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.models.MealData
import com.pappt04.menzans.models.MealPeriod
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
import java.time.LocalDate
import java.util.Calendar

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

    private val _currentMeal = MutableStateFlow<String?>(null)
    val currentMeal: StateFlow<String?> = _currentMeal.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _refreshTick = MutableStateFlow(0)
    val refreshTick: StateFlow<Int> = _refreshTick.asStateFlow()

    fun saveMealCounts(prefs: MealPreferences) {
        viewModelScope.launch { mealRepository.saveMealCounts(prefs) }
    }

    /**
     * Returns (dayOfWeek 0=Mon…6=Sun, meal API name) matching the current-or-next meal period,
     * using the same logic as the menu card. After all meals end for the day, returns tomorrow's
     * breakfast so the graph is always populated.
     */
    private fun detectCurrentOrNextMeal(): Pair<Int, String> {
        val cal = Calendar.getInstance()
        val nowMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        val period = MealPeriod.entries.firstOrNull { nowMinutes < it.minuteEnd() }
            ?: MealPeriod.BREAKFAST // wrap-around: after dinner → tomorrow's breakfast

        val todayIndex = LocalDate.now().dayOfWeek.value - 1 // Monday=0, Sunday=6
        val pastAllMeals = period == MealPeriod.BREAKFAST &&
            nowMinutes >= MealPeriod.DINNER.minuteEnd()
        val day = if (pastAllMeals) (todayIndex + 1) % 7 else todayIndex

        val meal = when (period) {
            MealPeriod.BREAKFAST -> "breakfast"
            MealPeriod.LUNCH -> "lunch"
            MealPeriod.DINNER -> "dinner"
        }
        return Pair(day, meal)
    }

    fun fetchGraphData() {
        viewModelScope.launch {
            val (day, meal) = detectCurrentOrNextMeal()
            _currentMeal.value = meal
            _graphState.value = UiState.Loading
            val result = waitTimeRepository.getLineGraphForMeal(day, meal)
            _graphState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyMap())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _refreshTick.value++
            val (day, meal) = detectCurrentOrNextMeal()
            _currentMeal.value = meal
            val result = waitTimeRepository.getLineGraphForMeal(day, meal)
            _graphState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyMap())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
            _isRefreshing.value = false
        }
    }
}
