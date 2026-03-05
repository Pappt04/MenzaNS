package com.pappt04.menzans.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.repository.CardRepository
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(
    private val cardRepository: CardRepository,
    private val mealRepository: MealRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val cardInfo: StateFlow<CardPreferences> =
        cardRepository.getCardInfo().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            CardPreferences(),
        )

    val mealCounts: StateFlow<MealPreferences> = mealRepository.getMealCounts()
        .stateIn(viewModelScope, SharingStarted.Lazily, MealPreferences())

    val breakfastTokenWarning: StateFlow<Int> = settingsRepository.getSettings()
        .map { it.breakfastTokenWarning }
        .stateIn(viewModelScope, SharingStarted.Lazily, 2)

    val lunchTokenWarning: StateFlow<Int> = settingsRepository.getSettings()
        .map { it.lunchTokenWarning }
        .stateIn(viewModelScope, SharingStarted.Lazily, 2)

    val dinnerTokenWarning: StateFlow<Int> = settingsRepository.getSettings()
        .map { it.dinnerTokenWarning }
        .stateIn(viewModelScope, SharingStarted.Lazily, 2)

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

    fun shareCard(cardInfo: CardPreferences): Intent {
        val str =
            buildString {
                appendLine("Prezime: ${cardInfo.surname}")
                appendLine("Ime: ${cardInfo.name}")
                appendLine("Univerzitet: Univerzitet u Novom Sadu")
                appendLine("Fakultet: ${cardInfo.faculty}")
                appendLine("Indeks: ${cardInfo.index}")
                appendLine("Datum rođenja: ${cardInfo.dateofbirth}")
                appendLine("Datum Izdavanja: ${cardInfo.issued}")
                appendLine("Važi do: ${cardInfo.validuntil}")
                appendLine("Broj kartice: ${cardInfo.cardnumber}")
                appendLine("ISIC broj kartice: ${cardInfo.isicnumber}")
            }
        return Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, str)
                type = "text/plain"
            },
            null,
        )
    }
}
