@file:OptIn(DelicateCoroutinesApi::class)

package com.pappt04.menzans.data.settingsdatastorage

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun SettingsDataCoordinator.updateBoolean(value:Boolean,mapkey: String, key: Preferences.Key<Boolean>)
{
    this.booleanMap[mapkey]=value
    GlobalScope.launch(Dispatchers.Default) {
        setBooleanDataStore(value,key)
    }
}