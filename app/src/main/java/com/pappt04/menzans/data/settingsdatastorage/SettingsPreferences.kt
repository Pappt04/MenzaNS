package com.pappt04.menzans.data.settingsdatastorage

data class SettingsPreferences(
    var userID: String= "",
    var language: String = "",
    var darktheme: Boolean = false,
    var materialyoutheme: Boolean = false,
    var budget: Boolean = false,
    var tokenwarning: Int = 2,
)