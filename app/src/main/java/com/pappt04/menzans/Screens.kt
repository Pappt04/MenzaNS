package com.pappt04.menzans

sealed class Screen(val route: String) {
    object MainScreen: Screen("ScaffoldDesign")
    object EditScreen: Screen("EditScreen")
    object InfoScreen: Screen("InfoScreen")
    object SettingsScreen: Screen("SettingsScreen")
}
