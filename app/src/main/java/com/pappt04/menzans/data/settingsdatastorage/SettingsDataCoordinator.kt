@file:OptIn(DelicateCoroutinesApi::class)

package com.pappt04.menzans.data.settingsdatastorage

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.pappt04.menzans.data.DummyData.DARK_THEME_KEY
import com.pappt04.menzans.data.DummyData.FINANCING_KEY
import com.pappt04.menzans.data.DummyData.MATERIALYOU_THEME_KEY
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsDataCoordinator {
    companion object {
        val shared= SettingsDataCoordinator()
        const val identifier = "[DataCoordinator]"
    }
    var context: Context?=null

    val financingDefault=false
    val darkthemeDefault=false
    val materialyouDefault=false

    val booleanMap: MutableMap<String, Boolean> = mutableMapOf(
        FINANCING_KEY to financingDefault,
        DARK_THEME_KEY to darkthemeDefault,
        MATERIALYOU_THEME_KEY to materialyouDefault
    )

    private val USER_PREFERENCES="user_preferences"

    val Context.dataStore by preferencesDataStore(
        name= USER_PREFERENCES
    )

    fun initialize(context: Context, onLoad:() -> Unit) {
        Log.d(
            identifier, "Initialized"
        )
        this.context = context
        GlobalScope.launch(Dispatchers.Default) {
            booleanMap[FINANCING_KEY]= getBooleanDataStore(SettingsPreferenceKeys.FINANCING_PREFERENCE)
            booleanMap[DARK_THEME_KEY]= getBooleanDataStore(SettingsPreferenceKeys.DARK_THEME_PREFERENCE)
            booleanMap[MATERIALYOU_THEME_KEY]= getBooleanDataStore(SettingsPreferenceKeys.MATERIALYOU_THEME_PREFERENCE)
            onLoad()
        }
    }
}