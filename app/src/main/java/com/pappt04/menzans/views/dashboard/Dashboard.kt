package com.pappt04.menzans.views.dashboard

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.models.MealData
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.models.UiState
import com.pappt04.menzans.data.consts.MealSample.mealIcons
import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import com.pappt04.menzans.viewmodels.DashboardViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun DashboardScreen(
    meals: List<MealData>,
    remainingOnCard: SnapshotStateList<Int>,
    viewModel: DashboardViewModel = koinViewModel(),
    waitime: MutableIntState,
    padding: PaddingValues,
    snackbar: SnackbarHostState,
    lazyListState: LazyListState = rememberLazyListState()
) {


    val graphcardState by viewModel.graphState.collectAsState()

    val context = LocalContext.current
    val statisticsRepository: StatisticsRepository = koinInject()

    val mealmanager = remember { MealDataStoreManager(context) }

    val mealValueList = remember {
        MutableList(3) { index ->
            mutableIntStateOf(remainingOnCard[index])
        }
    }
    var showBalanceDialog: Boolean by remember { mutableStateOf(false) }
    val balance = remember { mutableIntStateOf(remainingOnCard[3]) }
    val scope = rememberCoroutineScope()

    val selectedCard = remember { mutableIntStateOf(99) }

    Box(
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
                        .fillMaxHeight()
                        .padding(10.dp)
                        .clickable { selectedCard.intValue = 99 },
                ) {
                    var i = 0
                    items(meals) { meal: MealData ->
                        val index = remember { mutableIntStateOf(i) }

                        AnimatedVisibility(
                            selectedCard.intValue != index.intValue,
                        ) {
                            MealCard(
                                meal,
                                mealValueList[index.intValue],
                                mealIcons[index.intValue]
                            ) {

                                if (selectedCard.intValue == index.intValue) {
                                    selectedCard.intValue = 99
                                } else {
                                    selectedCard.intValue = index.intValue
                                }
                            }
                        }

                        remainingOnCard[index.intValue] = mealValueList[index.intValue].intValue
                        remainingOnCard[3] = balance.intValue
                        i++
                        i %= 3
                    }
                }
            }
            item {
                AnimatedContent(targetState = selectedCard.intValue,
                    transitionSpec = {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    }) {
                    if (it in 0..meals.size)
                        DetailedMealCard(meals[it], mealValueList[it], balance,
                            onClicked = {
                                selectedCard.intValue = 99
                            },
                            noFunds = {
                                scope.launch {
                                    snackbar.showSnackbar(
                                        context.getString(R.string.not_enough_funds),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onChanged ={
                                scope.launch {
                                    if (mealValueList.size == 3) {
                                        val mealprefs = MealPreferences(
                                            breakfast = mealValueList[0].intValue,
                                            lunch = mealValueList[1].intValue,
                                            dinner = mealValueList[2].intValue,
                                            balance = balance.intValue,
                                        )
                                        mealmanager.saveToDataStore(mealprefs)
                                    }
                                }
                            },
                            onConsumeMeal = { meal ->
                                scope.launch {
                                    statisticsRepository.addMealEvent(meal)
                                }
                            }
                            )
                }
            }
            item {
                BalanceCard(balance)
            }
            item {
                WaitTimeCard(waitime) {
                    //viewModel.fetchGraphData()
                }
            }
            item {
                TodayMenuCard()
            }
            item {
                when (graphcardState) {
                    is UiState.Loading -> {
                        viewModel.fetchGraphData()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        val data = (graphcardState as UiState.Success).data
                        OutlinedCard(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            LineSizeGraph(data)
                        }
                    }

                    is UiState.Error -> {
                        val errorMessage = (graphcardState as UiState.Error).message
                        Text("Error: $errorMessage", color = Color.Red)
                    }

                    is UiState.Empty -> {
                        viewModel.fetchGraphData()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }



        if (showBalanceDialog) {
            BalanceDialog(
                onDismissRequest = {
                    showBalanceDialog = false
                    scope.launch {
                        val mealprefs = MealPreferences(
                            breakfast = mealValueList[0].intValue,
                            lunch = mealValueList[1].intValue,
                            dinner = mealValueList[2].intValue,
                            balance = balance.intValue,
                        )
                        mealmanager.saveToDataStore(mealprefs)
                    }
                },
                balance, LocalContext.current
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewScaffold() {
    MenzaNSTheme {
        val meals = listOf(
            MealData(Uitext.StringResource(R.string.breakfast), 56, 7, 0, 9, 30),
            MealData(Uitext.StringResource(R.string.lunch), 120, 11, 0, 15, 0),
            MealData(Uitext.StringResource(R.string.dinner), 90, 18, 0, 20, 30)
        )

        val remainingOnCard = remember { mutableStateListOf(50, 25, 75, 100) }
        val wt = remember { mutableIntStateOf(15) }
        val snack = remember { SnackbarHostState() }
        MaterialTheme {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(title = { Text("Statistics Screen Preview") })
                }
            ) { innerPadding ->
                DashboardScreen(
                    meals = meals,
                    remainingOnCard = remainingOnCard,
                    waitime = wt,
                    padding = innerPadding,
                    snackbar = snack
                )
            }
        }
    }
}
