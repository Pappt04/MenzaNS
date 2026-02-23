package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _statistics = MutableStateFlow<MutableList<EatingStatisticsData>>(mutableListOf())
    val statistics: StateFlow<MutableList<EatingStatisticsData>> = _statistics.asStateFlow()

    fun loadStatistics(month: String) {
        viewModelScope.launch {
            val stats = statisticsRepository.getStatisticsForMonth(month)
            _statistics.value = stats
        }
    }

    fun addMealEvent(meal: MealEventString, month: String, mealData: EatingStatisticsData) {
        viewModelScope.launch {
            statisticsRepository.addMealEvent(meal, month, mealData)
            loadStatistics(month)
        }
    }

    fun removeMealEvent(meal: MealEventString, data: EatingStatisticsData, month: String) {
        viewModelScope.launch {
            statisticsRepository.removeMealEvent(meal, data, month, _statistics.value)
            loadStatistics(month)
        }
    }
}
