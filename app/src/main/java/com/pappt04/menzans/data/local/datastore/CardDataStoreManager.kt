package com.pappt04.menzans.data.local.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pappt04.menzans.models.CardPreferences
import kotlinx.coroutines.flow.map

const val CARD_DATASTORE = "card_data"

val Context.cardpreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = CARD_DATASTORE)

class CardDataStoreManager(
    val context: Context,
) {
    companion object {
        val SURNAME = stringPreferencesKey("SURNAME")
        val NAME = stringPreferencesKey("NAME")
        val INDEX = stringPreferencesKey("INDEX")
        val CARDNUMBER = stringPreferencesKey("CARDNUMBER")
        val ISICNUMBER = stringPreferencesKey("ISICNUMBER")
        val FACULTY = stringPreferencesKey("FACULTY")
        val DATEOFBIRTH = stringPreferencesKey("DATEOFBIRTH")
        val ISSUED = stringPreferencesKey("ISSUED")
        val VALIDUNTIL = stringPreferencesKey("VALIDUNTIL")
    }

    suspend fun saveToDataStore(cardprefs: CardPreferences) {
        Log.d("CARD_DATASTORE_MANAGER", "Successfully saved to Datastore")
        context.cardpreferenceDataStore.edit {
            it[SURNAME] = cardprefs.surname
            it[NAME] = cardprefs.name
            it[INDEX] = cardprefs.index
            it[CARDNUMBER] = cardprefs.cardnumber
            it[ISICNUMBER] = cardprefs.isicnumber
            it[FACULTY] = cardprefs.faculty
            it[DATEOFBIRTH] = cardprefs.dateofbirth
            it[ISSUED] = cardprefs.issued
            it[VALIDUNTIL] = cardprefs.validuntil
        }
    }

    fun getFromDataStore() =
        context.cardpreferenceDataStore.data.map {
            Log.d("CARD_DATASTORE_MANAGER", "Successfully loaded from Datastore")

            val def = ""
            CardPreferences(
                surname = it[SURNAME] ?: def,
                name = it[NAME] ?: def,
                index = it[INDEX] ?: def,
                cardnumber = it[CARDNUMBER] ?: def,
                isicnumber = it[ISICNUMBER] ?: def,
                faculty = it[FACULTY] ?: def,
                dateofbirth = it[DATEOFBIRTH] ?: def,
                issued = it[ISSUED] ?: def,
                validuntil = it[VALIDUNTIL] ?: def,
            )
        }

    suspend fun clearDataStore() =
        context.cardpreferenceDataStore.edit {
            Log.d("CARD_DATASTORE_MANAGER", "Successfully cleared Datastore")
            it.clear()
        }
}
