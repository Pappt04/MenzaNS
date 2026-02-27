package com.pappt04.menzans.repository

import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import com.pappt04.menzans.models.MealPreferences
import kotlinx.coroutines.flow.Flow

class MealRepository(
    private val mealDataStoreManager: MealDataStoreManager,
) {
    fun getMealCounts(): Flow<MealPreferences> = mealDataStoreManager.getFromDataStore()

    suspend fun saveMealCounts(prefs: MealPreferences) {
        mealDataStoreManager.saveToDataStore(prefs)
    }

    suspend fun clearAll() {
        mealDataStoreManager.clearDataStore()
    }
}
