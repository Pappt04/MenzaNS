package com.pappt04.menzans.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.stateIn
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.repository.CardRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardViewModel(
    private val cardRepository: CardRepository,
) : ViewModel() {

    val cardInfo: StateFlow<CardPreferences> = cardRepository.getCardInfo().stateIn(
        viewModelScope,
        kotlinx.coroutines.flow.SharingStarted.Lazily,
        CardPreferences()
    )

    fun saveCardInfo(prefs: CardPreferences) {
        viewModelScope.launch {
            cardRepository.saveCardInfo(prefs)
        }
    }

    fun clearCardInfo() {
        viewModelScope.launch {
            cardRepository.clearCardInfo()
        }
    }
}
