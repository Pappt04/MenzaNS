package com.pappt04.menzans.models

data class SettingsPreferences(
    var userID: String = "",
    var language: String = "",
    var darktheme: Boolean = false,
    var materialyoutheme: Boolean = false,
    var budget: Boolean = false,
    var tokenwarning: Int = 2,
    var firstWelcome: Boolean = true,
    var geofenceEnabled: Boolean = true,
    var eatingSpeedThreshold: Int = 15,
    var autoDeduct: Boolean = true,
)
