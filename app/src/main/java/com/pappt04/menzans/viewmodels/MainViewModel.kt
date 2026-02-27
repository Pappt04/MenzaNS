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
    val tokenWarning: Int = 2,
    val userId: String = "",
    val isFirstWelcome: Boolean = true,
    val savedMeals: List<Int> = listOf(0, 0, 0, 0),
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
                    tokenWarning = settings.tokenwarning,
                    userId = userId,
                    isFirstWelcome = settings.firstWelcome,
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

    fun updateTokenWarning(value: Int) {
        _uiState.value = _uiState.value.copy(tokenWarning = value)
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
                    tokenwarning = state.tokenWarning,
                    firstWelcome = state.isFirstWelcome,
                ),
            )
        }
    }
}
