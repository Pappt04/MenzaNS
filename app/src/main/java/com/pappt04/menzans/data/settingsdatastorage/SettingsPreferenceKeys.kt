package com.pappt04.menzans.data.settingsdatastorage

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.pappt04.menzans.data.DummyData.DARK_THEME_KEY
import com.pappt04.menzans.data.DummyData.FINANCING_KEY
import com.pappt04.menzans.data.DummyData.MATERIALYOU_THEME_KEY

data object SettingsPreferenceKeys {

    val FINANCING_PREFERENCE = booleanPreferencesKey(FINANCING_KEY)
    val DARK_THEME_PREFERENCE = booleanPreferencesKey(DARK_THEME_KEY)
    val MATERIALYOU_THEME_PREFERENCE = booleanPreferencesKey(MATERIALYOU_THEME_KEY)
}