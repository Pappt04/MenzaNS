package com.pappt04.menzans

sealed class Screen(val route: String) {
    data object MainScreen: Screen("ScaffoldDesign")
    data object StatisticsScreen: Screen("StatisticsScreen")
    data object EditScreen: Screen("EditScreen")
    data object InfoScreen: Screen("InfoScreen")
    data object SettingsScreen: Screen("SettingsScreen")
}
