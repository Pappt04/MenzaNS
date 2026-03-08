package com.pappt04.menzans.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pappt04.menzans.models.MealPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val MEAL_DATASTORE = "meal_data"

val Context.mealpreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = MEAL_DATASTORE)

class MealDataStoreManager(
    val context: Context,
) {
    companion object {
        val BREAKFAST = intPreferencesKey("BREAKFAST")
        val LUNCH = intPreferencesKey("LUNCH")
        val DINNER = intPreferencesKey("DINNER")
        val BALANCE = intPreferencesKey("BALANCE")
    }

    suspend fun saveToDataStore(mealprefs: MealPreferences) {
        Log.d("MEAL_DATASTORE_MANAGER", "Successfully saved to Datastore")
        context.mealpreferenceDataStore.edit {
            it[BREAKFAST] = mealprefs.breakfast
            it[LUNCH] = mealprefs.lunch
            it[DINNER] = mealprefs.dinner
            it[BALANCE] = mealprefs.balance
        }
    }

    fun getFromDataStore() =
        context.mealpreferenceDataStore.data.map {
            Log.d("MEAL_DATASTORE_MANAGER", "Successfully loaded from Datastore")
            MealPreferences(
                breakfast = it[BREAKFAST] ?: 0,
                lunch = it[LUNCH] ?: 0,
                dinner = it[DINNER] ?: 0,
                balance = it[BALANCE] ?: 0,
            )
        }

    /**
     * Atomically decrements the meal count for the given index (0=breakfast, 1=lunch, 2=dinner).
     * Returns the new count, or 0 if there were no tokens to deduct.
     * The entire read-modify-write runs inside a single DataStore edit transaction,
     * preventing concurrent geofence events from deducting the same token twice.
     */
    suspend fun decrementMealCount(mealIndex: Int): Int {
        var newCount = 0
        context.mealpreferenceDataStore.edit { prefs ->
            val current = when (mealIndex) {
                0 -> prefs[BREAKFAST] ?: 0
                1 -> prefs[LUNCH] ?: 0
                2 -> prefs[DINNER] ?: 0
                else -> 0
            }
            if (current > 0) {
                newCount = current - 1
                when (mealIndex) {
                    0 -> prefs[BREAKFAST] = newCount
                    1 -> prefs[LUNCH] = newCount
                    2 -> prefs[DINNER] = newCount
                }
            }
        }
        return newCount
    }

    suspend fun clearDataStore() =
        context.mealpreferenceDataStore.edit {
            Log.d("MEAL_DATASTORE_MANAGER", "Successfully cleared Datastore")
            it.clear()
        }
}
