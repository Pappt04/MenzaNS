package com.pappt04.menzans.views.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.pappt04.menzans.data.consts.NavigationConstants
import com.pappt04.menzans.views.common.AutoResizedText

@Composable
fun MenzaBottomNavigation(navController: NavHostController) {
    val context = LocalContext.current

    val selectedNavigationIndex = remember { mutableIntStateOf(0) }

    NavigationBar {
        NavigationConstants.navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    when (selectedNavigationIndex.intValue == index) {
                        true -> {
                            Icon(
                                imageVector = item.selectedIcon,
                                contentDescription = item.route,
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = item.unselectedIcon,
                                contentDescription = item.route,
                            )
                        }
                    }
                },
                label = {
                    AutoResizedText(item.title.asString(context))
                },
                alwaysShowLabel = false,
            )
        }
    }
}
