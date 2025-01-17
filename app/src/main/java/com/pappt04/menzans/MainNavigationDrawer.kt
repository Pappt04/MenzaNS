package com.pappt04.menzans

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
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
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationDrawer(cardData: List<String>, darkTheme: MutableState<Boolean>, savedMeals: SnapshotStateList<Int>) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val waittime= remember { mutableIntStateOf(999) }

    val trajectory= remember { mutableIntStateOf(0) }

    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    var welcome by remember { mutableStateOf(false) }

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
                    Text(
                        screenTitle,
                        fontWeight = FontWeight.Bold,
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
                }, actions = {

                   MinuteTicker {
                       getWaitTime(context) { wt ->
                           if (wt != null) {
                               var temp= waittime.intValue
                               waittime.intValue = wt.waittime.toInt()

                               if( temp == 999) {
                                 trajectory.intValue=wt.trajectory.toInt()
                               } else if(temp <waittime.intValue) {
                                   trajectory.intValue=1
                               } else if (temp > waittime.intValue) {
                                   trajectory.intValue=-1
                               } else {
                                   trajectory.intValue=0
                               }
                           }
                       }
                   }
                    WaitTimeDisplay(waittime.intValue,trajectory.intValue)
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
                    if (welcome) {
                        WelcomeDialog(
                            onDismissRequest = {
                                welcome = false }, context
                        )
                    }

                    DashboardDesign(MealSample,savedMeals)
                }
                composable(route = Screen.StatisticsScreen.route) {

                    StatisticsScreen(innerpadding)
                }
                composable(route = Screen.EditScreen.route) {

                    EditScreen(cardData, savedMeals, innerpadding)

                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen(innerpadding)
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen(innerpadding, darkTheme)
                }
            }
        }
    }
}

@Composable
fun MinuteTicker(onTick: () -> Unit) {
    LaunchedEffect(Unit) {
        while (true) {
            onTick()
            delay(60_000L) // Delay for 60 seconds
        }
    }
}

@Composable
fun WaitTimeDisplay(waitTime: Int, trajectory: Int) {
    val textColor = when (trajectory) {
        -1 -> Color.Green // Green for downward trend
        1 -> Color.Red     // Red for upward trend
        else -> MaterialTheme.colorScheme.secondary // Default color
    }

    val arrowIcon = when (trajectory) {
        1 -> Icons.Filled.KeyboardArrowUp
        -1 -> Icons.Filled.KeyboardArrowDown
        else -> null // No arrow if trajectory is 0 or other values
    }

    val str = when (waitTime) {
        999 -> "Wait time: No data"
        else -> "Wait time: $waitTime min"
    }

    Row(verticalAlignment = Alignment.CenterVertically) { // Use Row for icon and text
        Text(
            text = str,
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(end = 4.dp) // Add spacing between text and icon
        )
        if (arrowIcon != null) {
            Icon(
                imageVector = arrowIcon,
                contentDescription = if (trajectory == -1) "Downward Trend" else "Upward Trend",
                tint = textColor // Match icon color to text color
            )
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

    MenzaNSTheme {
        MainNavigationDrawer(cardData,darkTheme,savedMeals)
    }
}