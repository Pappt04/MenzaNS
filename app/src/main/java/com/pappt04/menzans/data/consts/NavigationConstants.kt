package com.pappt04.menzans.data.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import com.pappt04.menzans.R
import com.pappt04.menzans.models.NavigationItem
import com.pappt04.menzans.models.Uitext

object NavigationConstants {
    val navItems =
        listOf(
            NavigationItem(
                title = Uitext.StringResource(R.string.dashboard),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                route = "DashboardScreen",
            ),
            NavigationItem(
                title = Uitext.StringResource(R.string.statistics),
                selectedIcon = Icons.AutoMirrored.Filled.ShowChart,
                unselectedIcon = Icons.AutoMirrored.Outlined.ShowChart,
                route = "StatisticsScreen",
            ),
            NavigationItem(
                title = Uitext.StringResource(R.string.info),
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info,
                route = "InfoScreen",
            ),
            NavigationItem(
                title = Uitext.StringResource(R.string.card),
                selectedIcon = Icons.Filled.CreditCard,
                unselectedIcon = Icons.Outlined.CreditCard,
                route = "CardScreen",
            ),
            NavigationItem(
                title = Uitext.StringResource(R.string.settings),
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                route = "SettingsScreen",
            ),
        )
}
