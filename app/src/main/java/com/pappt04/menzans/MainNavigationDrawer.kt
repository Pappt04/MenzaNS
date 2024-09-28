package com.pappt04.menzans

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pappt04.menzans.DummyData.CardHolderFileName
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationDrawer(cardData: List<String>, darkTheme:MutableState<Boolean>) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }

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
                Text(text = stringResource(id=R.string.app_name), fontSize = 40.sp)
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
                            imageVector = Icons.Default.Menu, contentDescription = stringResource(R.string.menu_description)
                        )
                    }
                })
            },
        ) {
            innerpadding->
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
                    val files: Array<String> = context.fileList()
                    var remainingOnCard: Array<Int> = emptyArray()
                    var s1 = ""
                    for (s in DummyData.FileNames) {
                        if (s in files) {
                            context.openFileInput(s).bufferedReader().useLines { lines ->
                                lines.fold("") { some, text ->
                                    s1 = "$some$text"
                                    s1
                                }
                            }
                        } else {
                            s1 = "0"
                            context.openFileOutput(s, Context.MODE_PRIVATE).use {
                                it.write(s1.toByteArray())
                            }
                        }
                        remainingOnCard += s1.toInt()
                    }
                    val jsonMeals: List<MealData> = MealSample
                    ScaffoldDesign(jsonMeals, remainingOnCard)
                }
                composable(route = Screen.StatisticsScreen.route) {
                    StatisticsScreen(innerpadding)
                }

                composable(route = Screen.EditScreen.route) {
                    val files: Array<String> = context.fileList()
                    var stemp = ""
                    if (CardHolderFileName in files) {
                        context.openFileInput(CardHolderFileName).bufferedReader()
                            .useLines { lines ->
                                lines.fold("") { some, text ->
                                    stemp = "$some$text"
                                    stemp
                                }
                            }
                    } else {
                        stemp=",,,,,,,,"
                        context.openFileOutput(CardHolderFileName, Context.MODE_PRIVATE).use {
                            it.write(stemp.toByteArray())
                        }
                    }
                    val splitstring: List<String> = stemp.split(",")


                    var remainingOnCard: Array<Int> = emptyArray()
                    var s1 = ""
                    for (s in DummyData.FileNames) {
                        if (s in files) {
                            context.openFileInput(s).bufferedReader().useLines { lines ->
                                lines.fold("") { some, text ->
                                    s1 = "$some$text"
                                    s1
                                }
                            }
                        } else {
                            s1 = "0"
                            context.openFileOutput(s, Context.MODE_PRIVATE).use {
                                it.write(s1.toByteArray())
                            }
                        }
                        remainingOnCard += s1.toInt()
                    }
                    val jsonMeals: List<MealData> = MealSample

                    EditScreen(splitstring, remainingOnCard,jsonMeals,innerpadding)

                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen(innerpadding)
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen(innerpadding,darkTheme)
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
    MenzaNSTheme {
        //Navigation()
        //SideNavigationDrawer()
    }
}