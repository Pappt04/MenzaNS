package com.pappt04.menzans.views.navigation

sealed class Screen(
    val route: String,
) {
    data object DashboardScreen : Screen("DashboardScreen")

    data object StatisticsScreen : Screen("StatisticsScreen")

    data object InfoScreen : Screen("InfoScreen")

    data object CardScreen : Screen("CardScreen")

    data object SettingsScreen : Screen("SettingsScreen")
}
