package com.pappt04.menzans.appui.bottomnavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.pappt04.menzans.appui.animations.AutoResizedText
import com.pappt04.menzans.data.DummyData

@Composable
fun MenzaBottomNavigation(
    navController: NavHostController
) {
    val context = LocalContext.current

    var selectedNavigationIndex = remember { mutableIntStateOf(0) }

    NavigationBar(
    ) {
        DummyData.bottomNavItemData.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route)
                },
                icon = {
                    when (selectedNavigationIndex.intValue == index) {
                        true -> Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = item.route
                        )

                        else -> Icon(
                            imageVector = item.unselectedIcon,
                            contentDescription = item.route
                        )
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
