package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.UiState
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.WaitTimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val mealRepository: MealRepository,
    private val waitTimeRepository: WaitTimeRepository,
) : ViewModel() {
    private val _graphState = MutableStateFlow<UiState>(UiState.Empty)
    val graphState: StateFlow<UiState> = _graphState.asStateFlow()

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
