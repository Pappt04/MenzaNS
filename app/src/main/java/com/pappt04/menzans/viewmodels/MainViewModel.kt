package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.models.SettingsPreferences
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.SettingsRepository
import com.pappt04.menzans.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoaded: Boolean = false,
    val darkTheme: Boolean = false,
    val materialYouTheme: Boolean = false,
    val onBudgetPricing: Boolean = false,
    val breakfastTokenWarning: Int = 2,
    val lunchTokenWarning: Int = 2,
    val dinnerTokenWarning: Int = 2,
    val userId: String = "",
    val isFirstWelcome: Boolean = true,
    val savedMeals: List<Int> = listOf(0, 0, 0, 0),
    val geofenceEnabled: Boolean = true,
    val eatingSpeedThreshold: Int = 15,
    val autoDeduct: Boolean = true,
    val breakfastNotifyThreshold: Int = 70,
    val lunchNotifyThreshold: Int = 70,
    val dinnerNotifyThreshold: Int = 70,
    val geofenceRadius: Float = 25f,
)

class MainViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository,
    private val mealRepository: MealRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings().first()
            val mealPrefs = mealRepository.getMealCounts().first()

            userRepository.loadOrRegisterUser()
            val userId = userRepository.getUserId()

            _uiState.value =
                _uiState.value.copy(
                    darkTheme = settings.darktheme,
                    materialYouTheme = settings.materialyoutheme,
                    onBudgetPricing = settings.budget,
                    breakfastTokenWarning = settings.breakfastTokenWarning,
                    lunchTokenWarning = settings.lunchTokenWarning,
                    dinnerTokenWarning = settings.dinnerTokenWarning,
                    userId = userId,
                    isFirstWelcome = settings.firstWelcome,
                    geofenceEnabled = settings.geofenceEnabled,
                    eatingSpeedThreshold = settings.eatingSpeedThreshold,
                    autoDeduct = settings.autoDeduct,
                    breakfastNotifyThreshold = settings.breakfastNotifyThreshold,
                    lunchNotifyThreshold = settings.lunchNotifyThreshold,
                    dinnerNotifyThreshold = settings.dinnerNotifyThreshold,
                    geofenceRadius = settings.geofenceRadius,
                    savedMeals =
                        listOf(
                            mealPrefs.breakfast,
                            mealPrefs.lunch,
                            mealPrefs.dinner,
                            mealPrefs.balance,
                        ),
                    isLoaded = true,
                )
        }
    }

    fun updateDarkTheme(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(darkTheme = enabled)
        persistSettings()
    }

    fun updateMaterialYou(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(materialYouTheme = enabled)
        persistSettings()
    }

    fun updateBudgetPricing(onBudget: Boolean) {
        _uiState.value = _uiState.value.copy(onBudgetPricing = onBudget)
        persistSettings()
    }

    fun updateBreakfastTokenWarning(value: Int) {
        _uiState.value = _uiState.value.copy(breakfastTokenWarning = value)
        persistSettings()
    }

    fun updateLunchTokenWarning(value: Int) {
        _uiState.value = _uiState.value.copy(lunchTokenWarning = value)
        persistSettings()
    }

    fun updateDinnerTokenWarning(value: Int) {
        _uiState.value = _uiState.value.copy(dinnerTokenWarning = value)
        persistSettings()
    }

    fun updateGeofenceEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(geofenceEnabled = enabled)
        persistSettings()
    }

    fun updateEatingSpeedThreshold(minutes: Int) {
        _uiState.value = _uiState.value.copy(eatingSpeedThreshold = minutes)
        persistSettings()
    }

    fun updateAutoDeduct(autoDeduct: Boolean) {
        _uiState.value = _uiState.value.copy(autoDeduct = autoDeduct)
        persistSettings()
    }

    fun updateBreakfastNotifyThreshold(value: Int) {
        _uiState.value = _uiState.value.copy(breakfastNotifyThreshold = value)
        persistSettings()
    }

    fun updateLunchNotifyThreshold(value: Int) {
        _uiState.value = _uiState.value.copy(lunchNotifyThreshold = value)
        persistSettings()
    }

    fun updateDinnerNotifyThreshold(value: Int) {
        _uiState.value = _uiState.value.copy(dinnerNotifyThreshold = value)
        persistSettings()
    }

    fun updateGeofenceRadius(radius: Float) {
        _uiState.value = _uiState.value.copy(geofenceRadius = radius)
        persistSettings()
    }

    fun setFirstWelcomeComplete() {
        _uiState.value = _uiState.value.copy(isFirstWelcome = false)
        persistSettings()
    }

    fun updateMealCounts(mealPrefs: MealPreferences) {
        viewModelScope.launch {
            mealRepository.saveMealCounts(mealPrefs)
        }
        _uiState.value =
            _uiState.value.copy(
                savedMeals =
                    listOf(
                        mealPrefs.breakfast,
                        mealPrefs.lunch,
                        mealPrefs.dinner,
                        mealPrefs.balance,
                    ),
            )
    }

    private fun persistSettings() {
        viewModelScope.launch {
            val state = _uiState.value
            settingsRepository.saveSettings(
                SettingsPreferences(
                    userID = state.userId,
                    darktheme = state.darkTheme,
                    materialyoutheme = state.materialYouTheme,
                    budget = state.onBudgetPricing,
                    breakfastTokenWarning = state.breakfastTokenWarning,
                    lunchTokenWarning = state.lunchTokenWarning,
                    dinnerTokenWarning = state.dinnerTokenWarning,
                    firstWelcome = state.isFirstWelcome,
                    geofenceEnabled = state.geofenceEnabled,
                    eatingSpeedThreshold = state.eatingSpeedThreshold,
                    autoDeduct = state.autoDeduct,
                    breakfastNotifyThreshold = state.breakfastNotifyThreshold,
                    lunchNotifyThreshold = state.lunchNotifyThreshold,
                    dinnerNotifyThreshold = state.dinnerNotifyThreshold,
                    geofenceRadius = state.geofenceRadius,
                ),
            )
        }
    }
}
