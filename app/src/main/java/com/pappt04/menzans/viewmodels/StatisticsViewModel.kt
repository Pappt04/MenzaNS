package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
) : ViewModel() {
    private val _statistics = MutableStateFlow<List<EatingStatisticsData>>(emptyList())
    val statistics: StateFlow<List<EatingStatisticsData>> = _statistics.asStateFlow()

    fun loadStatistics(month: String) {
        viewModelScope.launch {
            _statistics.value = statisticsRepository.getStatisticsForMonth(month)
        }
    }

    fun addMealEvent(
        mealData: EatingStatisticsData,
        month: String,
    ) {
        viewModelScope.launch {
            statisticsRepository.addMealEvent(mealData)
            loadStatistics(month)
        }
    }

    fun removeMealEvent(
        data: EatingStatisticsData,
        month: String,
    ) {
        viewModelScope.launch {
            statisticsRepository.removeMealEvent(data)
            loadStatistics(month)
        }
    }
}
