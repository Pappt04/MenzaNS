package com.pappt04.menzans.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.repository.CardRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(
    private val cardRepository: CardRepository,
) : ViewModel() {
    val cardInfo: StateFlow<CardPreferences> =
        cardRepository.getCardInfo().stateIn(
            viewModelScope,
            kotlinx.coroutines.flow.SharingStarted.Lazily,
            CardPreferences(),
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
