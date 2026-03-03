package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.R
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
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

    private val _error = MutableStateFlow<Uitext?>(null)
    val error: StateFlow<Uitext?> = _error.asStateFlow()

    // Prevents concurrent duplicate fetches (#1, #10)
    private var isFetchingToday = false

    fun fetchTodayMenu() {
        if (_menu.value != null) return   // Already have data — use cache (#5)
        if (isFetchingToday) return       // Already in flight (#10)
        isFetchingToday = true
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = menuRepository.getTodayMenu()
                if (result.isSuccess) {
                    _menu.value = result.getOrNull()
                } else {
                    _error.value = mapError(result.exceptionOrNull())
                }
                _isLoading.value = false
            } finally {
                isFetchingToday = false
            }
        }
    }

    // --- Week menu (used by MenuTab in InfoScreen) ---

    private val _weekMenu = MutableStateFlow<Map<String, DayMenu>?>(null)
    val weekMenu: StateFlow<Map<String, DayMenu>?> = _weekMenu.asStateFlow()

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()

    private val _weekLoading = MutableStateFlow(true)
    val weekLoading: StateFlow<Boolean> = _weekLoading.asStateFlow()

    private val _weekError = MutableStateFlow<Uitext?>(null)
    val weekError: StateFlow<Uitext?> = _weekError.asStateFlow()

    val selectedDayMenu: StateFlow<DayMenu?> =
        combine(_weekMenu, _selectedDate) { week, date -> week?.get(date) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private var isFetchingWeek = false

    fun fetchWeekMenu() {
        if (_weekMenu.value != null) return  // Already have data — use cache
        if (isFetchingWeek) return           // Already in flight
        isFetchingWeek = true
        viewModelScope.launch {
            try {
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
                    _weekError.value = mapError(result.exceptionOrNull())
                }
                _weekLoading.value = false
            } finally {
                isFetchingWeek = false
            }
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

        /** Maps network/HTTP exceptions to localized, user-readable error text (#6). */
        fun mapError(e: Throwable?): Uitext = when (e) {
            is SocketTimeoutException -> Uitext.StringResource(R.string.menu_error_timeout)
            is IOException            -> Uitext.StringResource(R.string.menu_error_network)
            else                      -> Uitext.StringResource(R.string.menu_error_server)
        }
    }
}
