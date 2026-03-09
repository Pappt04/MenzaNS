package com.pappt04.menzans.views.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pappt04.menzans.viewmodels.MainViewModel
import com.pappt04.menzans.views.card.CardScreen
import com.pappt04.menzans.views.common.AnimatedAppearance
import com.pappt04.menzans.views.dashboard.DashboardScreen
import com.pappt04.menzans.views.info.InfoScreen
import com.pappt04.menzans.views.settings.SettingsScreen
import com.pappt04.menzans.views.statistics.StatisticsScreen
import com.pappt04.menzans.views.welcome.WelcomeScreen
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MenzaScaffold(
    mainViewModel: MainViewModel,
    drawerState: DrawerState,
    screenTitle: String,
    selectedItemIndex: MutableState<Int>,
    navController: NavHostController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val bottomController = rememberNavController()
    val state by mainViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AnimatedAppearance(enter = slideInVertically { -it }) {
                MenzaTopBar(state.isFirstWelcome, drawerState, screenTitle, state.userId)
            }
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        bottomBar = {
            AnimatedAppearance(
                enter = slideInVertically { it },
            ) {
                MenzaBottomNavigation(bottomController)
            }
        },
    ) { innerpadding ->

        NavHost(
            navController = bottomController,
            startDestination = Screen.DashboardScreen.route,
        ) {
            composable(route = Screen.DashboardScreen.route) {
                if (state.isFirstWelcome) {
                    WelcomeScreen(onCompleted = { mainViewModel.setFirstWelcomeComplete() }, innerpadding)
                } else {
                    AnimatedAppearance(
                        delay = 5.milliseconds,
                        enter = slideInVertically { it },
                    ) {
                        DashboardScreen(
                            padding = innerpadding,
                            snackbar = snackbarHostState,
                        )
                    }
                }
            }
            composable(route = Screen.StatisticsScreen.route) {
                AnimatedAppearance(enter = slideInVertically { it }) {
                    StatisticsScreen(innerpadding)
                }
            }
            composable(route = Screen.InfoScreen.route) {
                AnimatedAppearance(enter = slideInVertically { it }) {
                    InfoScreen(innerpadding)
                }
            }
            composable(route = Screen.CardScreen.route) {
                AnimatedAppearance(enter = slideInVertically { it }) {
                    CardScreen(snackbarHostState, innerpadding)
                }
            }
            composable(route = Screen.SettingsScreen.route) {
                AnimatedAppearance(enter = slideInVertically { it }) {
                    SettingsScreen(innerpadding, mainViewModel)
                }
            }
        }
    }
}
