package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.repository.SettingsRepository
import com.pappt04.menzans.repository.StatisticsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val onBudgetPricing: StateFlow<Boolean> = settingsRepository.getSettings()
        .map { it.budget }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _statistics = MutableStateFlow<List<EatingStatisticsData>>(emptyList())
    val statistics: StateFlow<List<EatingStatisticsData>> = _statistics.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _historyPage = MutableStateFlow<List<EatingStatisticsData>>(emptyList())
    val historyPage: StateFlow<List<EatingStatisticsData>> = _historyPage.asStateFlow()

    private val _totalEventCount = MutableStateFlow(0)
    val totalEventCount: StateFlow<Int> = _totalEventCount.asStateFlow()

    private var currentHistoryPage = 0
    private val pageSize = 50

    fun loadStatistics(month: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _statistics.value = statisticsRepository.getStatisticsForMonth(month)
            // Wait for Compose to recompose the charts and for Vico's LaunchedEffect
            // to run modelProducer.runTransaction() before removing the placeholder.
            // Without this delay the empty CartesianChartHost renders for several frames
            // and intercepts all touch events, breaking LazyColumn scroll.
            delay(400)
            _isLoading.value = false
        }
    }

    fun loadHistoryPage(page: Int) {
        viewModelScope.launch {
            currentHistoryPage = page
            _historyPage.value = statisticsRepository.getHistoryPage(page, pageSize)
            _totalEventCount.value = statisticsRepository.getTotalEventCount()
        }
    }

    fun loadNextHistoryPage() = loadHistoryPage(currentHistoryPage + 1)

    fun loadPreviousHistoryPage() = loadHistoryPage((currentHistoryPage - 1).coerceAtLeast(0))

    fun addMealEvent(mealData: EatingStatisticsData, month: String) {
        viewModelScope.launch {
            statisticsRepository.addMealEvent(mealData)
            loadStatistics(month)
        }
    }

    fun removeMealEvent(data: EatingStatisticsData, month: String) {
        viewModelScope.launch {
            statisticsRepository.removeMealEvent(data)
            loadStatistics(month)
        }
    }
}
