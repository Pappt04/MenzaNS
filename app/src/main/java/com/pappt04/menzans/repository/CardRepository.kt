package com.pappt04.menzans.repository

import com.pappt04.menzans.data.local.datastore.CardDataStoreManager
import com.pappt04.menzans.models.CardPreferences
import kotlinx.coroutines.flow.Flow

class CardRepository(
    private val cardDataStoreManager: CardDataStoreManager
) {
    fun getCardInfo(): Flow<CardPreferences> = cardDataStoreManager.getFromDataStore()

    suspend fun saveCardInfo(prefs: CardPreferences) {
        cardDataStoreManager.saveToDataStore(prefs)
    }

    suspend fun clearCardInfo() {
        cardDataStoreManager.clearDataStore()
    }
}
