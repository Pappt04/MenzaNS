package com.pappt04.menzans.views.navigation

import android.content.Context
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.pappt04.menzans.views.card.CardScreen
import com.pappt04.menzans.views.info.InfoScreen
import com.pappt04.menzans.views.common.AnimatedAppearance
import com.pappt04.menzans.views.dashboard.DashboardScreen
import com.pappt04.menzans.views.dashboard.GraphCardViewModel
import com.pappt04.menzans.views.settings.SettingsScreen
import com.pappt04.menzans.views.statistics.StatisticsScreen
import com.pappt04.menzans.views.welcome.WelcomeScreen
import com.pappt04.menzans.data.local.FileContainer.CardHolderFileName
import com.pappt04.menzans.data.local.StatisticsFileDAO
import com.pappt04.menzans.data.consts.DummyData.datetypemonth
import com.pappt04.menzans.data.consts.DummyData.engmonths
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.data.consts.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.viewmodels.MainViewModel
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MenzaScaffold(
    mainViewModel: MainViewModel,
    firstWelcome: MutableState<Boolean>,
    drawerState: DrawerState,
    screenTitle: String,
    selectedItemIndex: MutableState<Int>,
    navController: NavHostController,
    onBudgetPricing: MutableState<Boolean>,
    savedMeals: SnapshotStateList<Int>,
    waittime: MutableIntState,
) {
    val gcvm = remember { GraphCardViewModel() }

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    val bottomController = rememberNavController()

    val scope = rememberCoroutineScope()

    val statDAO = StatisticsFileDAO(context, engmonths[datetypemonth.format(Date()).toInt() - 1])

    Scaffold(
        topBar = {
            AnimatedAppearance(enter = slideInVertically { -it }) {
                MenzaTopBar(firstWelcome, waittime, drawerState, screenTitle)
            }
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            AnimatedAppearance(
                enter = slideInVertically { it },
            ) {
                MenzaBottomNavigation(bottomController)
            }
        }
    ) { innerpadding ->

        val graph =
            navController.createGraph(startDestination = Screen.DashboardScreen.route) {
                composable(route = Screen.DashboardScreen.route) {
                    if (firstWelcome.value) {
                        WelcomeScreen(onCompleted = { firstWelcome.value = false }, innerpadding)
                    } else {
                        AnimatedAppearance(
                            delay = 5.milliseconds,
                            enter = slideInVertically { it }) {
                            DashboardScreen(
                                when (onBudgetPricing.value) {
                                    true -> MealSampleBudget
                                    else -> MealSampleSelfFinancing
                                },
                                savedMeals,
                                gcvm,
                                waittime,
                                innerpadding,
                                snackbarHostState
                            )
                        }
                    }
                }
                composable(route = Screen.StatisticsScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        StatisticsScreen(innerpadding, onBudgetPricing, statDAO)
                    }
                }
                composable(route = Screen.InfoScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        InfoScreen(innerpadding)
                    }
                }
                composable(route = Screen.CardScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        CardScreen(savedMeals,snackbarHostState, innerpadding)
                    }
                }
                composable(route = Screen.SettingsScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        SettingsScreen(
                            innerpadding,
                            mainViewModel,
                            onBudgetPricing
                        )
                    }
                }
            }
        NavHost(
            navController = bottomController,
            graph = graph,
        )
        /*
                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                    selectedItemIndex.value = when (destination.route) {
                        "ScaffoldDesign" -> 0
                        "StatisticsScreen" -> 1
                        "InfoScreen" -> 2
                        "EditScreen" -> 3
                        "SettingsScreen" -> 4
                        else -> 0
                    }

                }
                NavHost(navController = navController, startDestination = Screen.DashboardScreen.route) {
                    composable(route = Screen.DashboardScreen.route) {
                        if (firstWelcome.value) {
                            WelcomeScreen(onCompleted = { firstWelcome.value = false }, innerpadding)
                        } else {
                            DashboardScreen(
                                when (onBudgetPricing.value) {
                                    true -> MealSampleBudget
                                    else -> MealSampleSelfFinancing
                                }, savedMeals, MyViewModel(), waittime, innerpadding, snackbarHostState
                            )
                        }
                    }
                    composable(route = Screen.StatisticsScreen.route) {

                        StatisticsScreen(innerpadding, onBudgetPricing)
                    }
                    composable(route = Screen.CardScreen.route) {

                        val cardData= loadCardHolder(context)
                        CardScreen(cardData, savedMeals, innerpadding)

                    }
                    composable(route = Screen.InfoScreen.route) {
                        InfoScreen(innerpadding)
                    }
                    composable(route = Screen.SettingsScreen.route) {
                        SettingsScreen(innerpadding, darkTheme, materialtheme, onBudgetPricing)
                    }
                }

         */
    }
}


fun loadCardHolder(context: Context): List<String> {
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
        stemp = ",,,,,,,,"
        context.openFileOutput(CardHolderFileName, Context.MODE_PRIVATE).use {
            it.write(stemp.toByteArray())
        }
    }
    return stemp.split(",")
}
