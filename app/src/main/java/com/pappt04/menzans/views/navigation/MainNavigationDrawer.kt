package com.pappt04.menzans.views.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.pappt04.menzans.R
import com.pappt04.menzans.viewmodels.MainViewModel

@Composable
fun MainNavigationDrawer(
    mainViewModel: MainViewModel,
    onBudgetPricing: MutableState<Boolean>,
    firstWelcome: MutableState<Boolean>,
    savedMeals: SnapshotStateList<Int>,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val waittime = remember { mutableIntStateOf(999) }
    val navController = rememberNavController()
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val screenTitle = when (selectedItemIndex.intValue) {
        0 -> stringResource(R.string.app_name)
        1 -> stringResource(R.string.statistics)
        2 -> stringResource(R.string.info)
        3 -> stringResource(R.string.card)
        else -> stringResource(R.string.settings)
    }
    MenzaScaffold(
        mainViewModel,
        firstWelcome,
        drawerState,
        screenTitle,
        selectedItemIndex,
        navController,
        onBudgetPricing,
        savedMeals,
        waittime,
    )
}


//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
//)
//@Composable
//fun PreviewSideNavigationDrawer() {
//    val darkTheme = remember { mutableStateOf(false) }
//    val savedMeals = remember { mutableStateListOf(1, 2, 3) }
//
//    val welcome= remember { mutableStateOf(false) }
//
//    MenzaNSTheme {
//        MainNavigationDrawer(darkTheme,darkTheme,darkTheme,welcome,savedMeals)
//    }
//}
