package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MenuViewModel(
    private val menuRepository: MenuRepository,
) : ViewModel() {

    // --- Today's menu (used by TodayMenuCard on Dashboard) ---

    private val _menu = MutableStateFlow<DayMenu?>(null)
    val menu: StateFlow<DayMenu?> = _menu.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchTodayMenu() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = menuRepository.getTodayMenu()
            if (result.isSuccess) {
                _menu.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }

    // --- Week menu (used by MenuTab in InfoScreen) ---

    private val _weekMenu = MutableStateFlow<Map<String, DayMenu>?>(null)
    val weekMenu: StateFlow<Map<String, DayMenu>?> = _weekMenu.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private val _weekLoading = MutableStateFlow(true)
    val weekLoading: StateFlow<Boolean> = _weekLoading.asStateFlow()

    private val _weekError = MutableStateFlow<String?>(null)
    val weekError: StateFlow<String?> = _weekError.asStateFlow()

    val selectedDayMenu: StateFlow<DayMenu?> =
        combine(_weekMenu, _selectedDate) { week, date -> week?.get(date) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun fetchWeekMenu() {
        if (_weekMenu.value != null) return
        viewModelScope.launch {
            _weekLoading.value = true
            _weekError.value = null
            val result = menuRepository.getWeekMenu()
            if (result.isSuccess) {
                val week = result.getOrNull()
                _weekMenu.value = week
                val todayKey = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
                _selectedDate.value = week?.keys?.find { it == todayKey }
                    ?: week?.keys?.sortedWith(dateKeyComparator)?.firstOrNull()
            } else {
                _weekError.value = result.exceptionOrNull()?.message
            }
            _weekLoading.value = false
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    companion object {
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateKeyComparator = Comparator<String> { a, b ->
            runCatching { dateFormat.parse(a)!!.compareTo(dateFormat.parse(b)!!) }.getOrDefault(0)
        }
    }
}
