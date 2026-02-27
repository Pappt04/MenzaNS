package com.pappt04.menzans.views.navigation

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.pappt04.menzans.views.card.CardScreen
import com.pappt04.menzans.views.info.InfoScreen
import com.pappt04.menzans.views.common.AnimatedAppearance
import com.pappt04.menzans.views.dashboard.DashboardScreen
import com.pappt04.menzans.views.settings.SettingsScreen
import com.pappt04.menzans.views.statistics.StatisticsScreen
import com.pappt04.menzans.views.welcome.WelcomeScreen
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.data.consts.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.viewmodels.MainViewModel
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
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomController = rememberNavController()

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
                        WelcomeScreen(onCompleted = { mainViewModel.setFirstWelcomeComplete() }, innerpadding)
                    } else {
                        AnimatedAppearance(
                            delay = 5.milliseconds,
                            enter = slideInVertically { it }) {
                            DashboardScreen(
                                meals = when (onBudgetPricing.value) {
                                    true -> MealSampleBudget
                                    else -> MealSampleSelfFinancing
                                },
                                remainingOnCard = savedMeals,
                                waitime = waittime,
                                padding = innerpadding,
                                snackbar = snackbarHostState
                            )
                        }
                    }
                }
                composable(route = Screen.StatisticsScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        StatisticsScreen(innerpadding, onBudgetPricing)
                    }
                }
                composable(route = Screen.InfoScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        InfoScreen(innerpadding)
                    }
                }
                composable(route = Screen.CardScreen.route) {
                    AnimatedAppearance(enter = slideInVertically { it }) {
                        CardScreen(savedMeals,mainViewModel.uiState.value.tokenWarning,snackbarHostState, innerpadding)
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
    }
}
