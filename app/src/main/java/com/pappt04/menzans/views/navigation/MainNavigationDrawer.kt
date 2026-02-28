package com.pappt04.menzans.views.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.pappt04.menzans.R
import com.pappt04.menzans.viewmodels.MainViewModel

@Composable
fun MainNavigationDrawer(mainViewModel: MainViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val navController = rememberNavController()
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val screenTitle =
        when (selectedItemIndex.intValue) {
            0 -> stringResource(R.string.app_name)
            1 -> stringResource(R.string.statistics)
            2 -> stringResource(R.string.info)
            3 -> stringResource(R.string.card)
            else -> stringResource(R.string.settings)
        }
    MenzaScaffold(
        mainViewModel,
        drawerState,
        screenTitle,
        selectedItemIndex,
        navController,
    )
}
