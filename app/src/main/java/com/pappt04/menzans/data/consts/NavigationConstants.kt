package com.pappt04.menzans.data.consts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import com.pappt04.menzans.R
import com.pappt04.menzans.models.NavigationItem
import com.pappt04.menzans.models.Uitext

object NavigationConstants {
    val navItems =
        listOf(
            NavigationItem(
                title = Uitext.StringResource(R.string.dashboard),
                selectedIcon = Icons.Filled.AccountCircle,
                unselectedIcon = Icons.Outlined.AccountCircle,
                route = "DashboardScreen",
            ),
            NavigationItem(
                title = Uitext.StringResource(R.string.statistics),
                selectedIcon = Icons.Default.LocationOn,
                unselectedIcon = Icons.Outlined.LocationOn,
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
