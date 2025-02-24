package com.pappt04.menzans.navigationdrawer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pappt04.menzans.EditScreen
import com.pappt04.menzans.InfoScreen
import com.pappt04.menzans.R
import com.pappt04.menzans.Screen
import com.pappt04.menzans.SettingsScreen
import com.pappt04.menzans.dashboard.DashboardScreen
import com.pappt04.menzans.dashboard.MyViewModel
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.DummyData.MealSampleBudget
import com.pappt04.menzans.data.DummyData.MealSampleSelfFinancing
import com.pappt04.menzans.statistics.StatisticsScreen
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import com.pappt04.menzans.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationDrawer(
    cardData: List<String>,
    darkTheme: MutableState<Boolean>,
    materialtheme: MutableState<Boolean>,
    onBudgetPricing: MutableState<Boolean>,
    firstWelcome: MutableState<Boolean>,
    savedMeals: SnapshotStateList<Int>,
    linegraphmap: Map<String,Double>
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val waittime = remember { mutableIntStateOf(999) }

    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    var mvm= MyViewModel()

    val screenTitle = when (selectedItemIndex) {
        0 -> stringResource(R.string.app_name)
        1 -> stringResource(R.string.statistics)
        2 -> stringResource(R.string.info)
        3 -> stringResource(R.string.edit)
        else -> stringResource(R.string.settings)
    }
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet(
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 64.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.app_name), fontSize = 40.sp)
            }

            //TODO CREATE A BETTER DESIGN FOR THIS CARD IN THIS STATE IT IS UNUSABLE
            //MenzaCard(cardData)
            HorizontalDivider(modifier = Modifier.padding(4.dp))
            DummyData.navigationItemData.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    selected = selectedItemIndex == index,
                    label = { Text(text = item.title.asString(context)) },
                    onClick = {
                        selectedItemIndex = index
                        scope.launch {
                            drawerState.close()

                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon, contentDescription = item.route
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }) {
        Scaffold(
            topBar = {
                TopAppBar(colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = {
                    if(!firstWelcome.value)
                    Text(
                        screenTitle,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }, navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu_description)
                        )
                    }
                }
                )
            },
        ) { innerpadding ->
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                selectedItemIndex = when (destination.route) {
                    "ScaffoldDesign" -> 0
                    "StatisticsScreen" -> 1
                    "InfoScreen" -> 2
                    "EditScreen" -> 3
                    "SettingsScreen" -> 4
                    else -> 0
                }

            }
            NavHost(navController = navController, startDestination = "ScaffoldDesign") {
                composable(route = Screen.MainScreen.route) {
                    if (firstWelcome.value) {
                        WelcomeScreen(onCompleted = {firstWelcome.value=false},innerpadding)
                    } else
                    {
                        DashboardScreen(when(onBudgetPricing.value) {
                            true -> MealSampleBudget
                            else -> MealSampleSelfFinancing
                        },savedMeals, MyViewModel(),waittime, innerpadding)
                    }
                }
                composable(route = Screen.StatisticsScreen.route) {

                    StatisticsScreen(innerpadding,onBudgetPricing)
                }
                composable(route = Screen.EditScreen.route) {

                    EditScreen(cardData, savedMeals, innerpadding)

                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen(innerpadding)
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen(innerpadding, darkTheme,materialtheme,onBudgetPricing)
                }
            }
        }
    }
}


@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode"
)
@Composable
fun PreviewSideNavigationDrawer() {
    val cardData = remember { listOf("Item 1", "Item 2", "Item 3") }
    val darkTheme = remember { mutableStateOf(false) }
    val savedMeals = remember { mutableStateListOf(1, 2, 3) }

    val welcome= remember { mutableStateOf(false) }

    val map = emptyMap<String,Double>()

    MenzaNSTheme {
        MainNavigationDrawer(cardData,darkTheme,darkTheme,darkTheme,welcome,savedMeals,map)
    }
}