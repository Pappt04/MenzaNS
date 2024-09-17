package com.pappt04.menzans

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun MainNavigationDrawer() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val navController = rememberNavController()

    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val screenTitle = when (selectedItemIndex) {
        0 -> "MenzaNS"
        1 -> "Edit Data on Card"
        2 -> "FYI stuff"
        3 -> "Settings"
        else -> "SettingsScreen"
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
                Text(text = "MenzaNS", fontSize = 40.sp)
            }
            DummyData.navigationItemData.forEachIndexed { index, item ->
                NavigationDrawerItem(
                    selected = selectedItemIndex == index,
                    label = { Text(text = item.title) },
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
                            imageVector = Icons.Default.Menu, contentDescription = "Menu"
                        )
                    }
                })
            },
        ) {
            it
            navController.addOnDestinationChangedListener() { controller, destination, arguments ->
                selectedItemIndex = when (destination.route) {
                    "ScaffoldDesign" -> 0
                    "EditScreen" -> 1
                    "InfoScreen" -> 2
                    "SettingsScreen" -> 3
                    else -> 0
                }

            }
            NavHost(navController = navController, startDestination = "ScaffoldDesign") {
                composable(route = Screen.MainScreen.route) {
                    var files: Array<String> = context.fileList()
                    var remainingOnCard: Array<Int> = emptyArray()
                    var s1: String = ""
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
                        context.openFileOutput(CardHolderFileName, Context.MODE_PRIVATE).use {
                        }
                    }
                    EditScreen()
                }
                composable(route = Screen.InfoScreen.route) {
                    InfoScreen()
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen()
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