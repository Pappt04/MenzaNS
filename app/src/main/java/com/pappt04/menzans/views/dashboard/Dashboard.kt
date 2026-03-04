package com.pappt04.menzans.views.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.Spacing
import com.pappt04.menzans.R
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.data.consts.MealSample.mealIcons
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.viewmodels.DashboardViewModel
import com.pappt04.menzans.viewmodels.MenuViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

const val NO_MEAL_SELECTED=-1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    padding: PaddingValues,
    snackbar: SnackbarHostState,
    viewModel: DashboardViewModel = koinViewModel(),
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val meals by viewModel.meals.collectAsState()
    val savedMealCounts by viewModel.mealCounts.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshTick by viewModel.refreshTick.collectAsState()

    val menuViewModel: MenuViewModel = koinViewModel()

    val context = LocalContext.current
    val statisticsRepository: StatisticsRepository = koinInject()

    val mealValueList = remember(savedMealCounts) {
        mutableListOf(
            mutableIntStateOf(savedMealCounts.breakfast),
            mutableIntStateOf(savedMealCounts.lunch),
            mutableIntStateOf(savedMealCounts.dinner),
        )
    }
    var showBalanceDialog: Boolean by remember { mutableStateOf(false) }
    val balance = remember(savedMealCounts.balance) { mutableIntStateOf(savedMealCounts.balance) }
    val scope = rememberCoroutineScope()
    val selectedCard = remember { mutableIntStateOf(NO_MEAL_SELECTED) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
            menuViewModel.fetchTodayMenu()
        },
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(padding)
    ) {
        LazyColumn {
            item {
                LazyRow(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                        .clickable { selectedCard.intValue = NO_MEAL_SELECTED },
                ) {
                    var i = 0
                    items(meals) { meal ->
                        val index = remember { mutableIntStateOf(i) }

                        AnimatedVisibility(selectedCard.intValue != index.intValue) {
                            MealCard(meal, mealValueList[index.intValue], mealIcons[index.intValue]) {
                                selectedCard.intValue =
                                    if (selectedCard.intValue == index.intValue) NO_MEAL_SELECTED else index.intValue
                            }
                        }
                        i++
                        i %= 3
                    }
                }
            }
            item {
                AnimatedContent(
                    targetState = selectedCard.intValue,
                    transitionSpec = { slideInVertically { -it } togetherWith slideOutVertically { it } }
                ) {
                    if (it in 0..meals.size)
                        DetailedMealCard(
                            meals[it],
                            mealValueList[it],
                            balance,
                            onClicked = { selectedCard.intValue = NO_MEAL_SELECTED },
                            noFunds = {
                                scope.launch {
                                    snackbar.showSnackbar(
                                        context.getString(R.string.not_enough_funds),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onChanged = {
                                if (mealValueList.size == 3) {
                                    viewModel.saveMealCounts(
                                        MealPreferences(
                                            breakfast = mealValueList[0].intValue,
                                            lunch = mealValueList[1].intValue,
                                            dinner = mealValueList[2].intValue,
                                            balance = balance.intValue,
                                        )
                                    )
                                }
                            },
                            onConsumeMeal = { meal ->
                                scope.launch { statisticsRepository.addMealEvent(meal) }
                            }
                        )
                }
            }
            item {
                BalanceCard(balance)
            }
            item {
                WaitTimeCard(refreshTrigger = refreshTick, snackbar = snackbar)
            }
            item {
                TodayMenuCard()
            }
            item {
                LineGraphCard(viewModel = viewModel)
            }
        }

        if (showBalanceDialog) {
            BalanceDialog(
                onDismissRequest = {
                    showBalanceDialog = false
                    viewModel.saveMealCounts(
                        MealPreferences(
                            breakfast = mealValueList[0].intValue,
                            lunch = mealValueList[1].intValue,
                            dinner = mealValueList[2].intValue,
                            balance = balance.intValue,
                        )
                    )
                },
                balance,
                LocalContext.current
            )
        }
    }
}
