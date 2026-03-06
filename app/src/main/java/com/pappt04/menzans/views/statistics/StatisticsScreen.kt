package com.pappt04.menzans.views.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.data.consts.CalendarData.monthFormat
import com.pappt04.menzans.ui.theme.Spacing
import com.pappt04.menzans.data.consts.CalendarData.monthNames
import com.pappt04.menzans.data.consts.MealSample
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.viewmodels.StatisticsViewModel
import com.pappt04.menzans.views.common.AnimatedAppearance
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.milliseconds
import java.time.Month
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Composable
fun StatisticsScreen(
    innerpadding: PaddingValues,
    viewModel: StatisticsViewModel = koinViewModel(),
) {
    val initialMonth = monthNames[monthFormat.format(Date()).toInt() - 1]

    var selectedMonth by remember { mutableStateOf<String?>(monthNames[monthFormat.format(Date()).toInt() - 1]) }

    val formattedStatisticsData by viewModel.statistics.collectAsState()
    val onBudget by viewModel.onBudgetPricing.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(selectedMonth) {
        viewModel.loadStatistics(selectedMonth ?: initialMonth)
    }

    LazyColumn(
        modifier =
            Modifier
                .padding(innerpadding)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Column {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                    contentPadding = PaddingValues(horizontal = Spacing.md),
                ) {
                    items(monthNames) { month ->
                        val localizedMonth =
                            Month.valueOf(month.uppercase()).getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault(),
                            )

                        FilterChip(
                            onClick = {
                                selectedMonth = if (selectedMonth == month) null else month
                            },
                            label = { Text(localizedMonth) },
                            selected = selectedMonth == month,
                        )
                    }
                }
                AnimatedVisibility(visible = selectedMonth != null) {
                    TextButton(
                        onClick = { selectedMonth = null },
                        modifier = Modifier.padding(horizontal = Spacing.md),
                    ) {
                        Text(stringResource(R.string.clear_month_filter))
                    }
                }
            }
        }

        item {
            AnimatedAppearance(delay = 50.milliseconds) {
                MonthlySummaryRow(
                    data = formattedStatisticsData,
                    onBudget = onBudget,
                )
            }
        }

        item {
            CalendarMonthView(selectedMonth ?: initialMonth, formattedStatisticsData, viewModel)
        }

        item {
            AnimatedAppearance(delay = 100.milliseconds) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
                ) {
                    Text(
                        stringResource(R.string.your_monthly_token_usage),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                            .align(Alignment.CenterHorizontally),
                    )
                    if (isLoading) {
                        ChartLoadingPlaceholder()
                    } else {
                        MonthlyMealsChart(formattedStatisticsData)
                    }
                }
            }
        }
        item {
            AnimatedAppearance(delay = 200.milliseconds) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
                ) {
                    Text(
                        stringResource(R.string.your_weekly_token_usage),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                            .align(Alignment.CenterHorizontally),
                    )
                    if (isLoading) {
                        ChartLoadingPlaceholder()
                    } else {
                        WeeklyMealChart(formattedStatisticsData)
                    }
                }
            }
        }
        item {
            AnimatedAppearance(delay = 300.milliseconds) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
                ) {
                    Text(
                        stringResource(R.string.predicted_spending),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                            .align(Alignment.CenterHorizontally),
                    )
                    if (isLoading) {
                        ChartLoadingPlaceholder()
                    } else {
                        PredictedSpendingChart(selectedMonth ?: initialMonth, onBudget, formattedStatisticsData)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChartLoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

private val tokenToIndex by lazy {
    MealSampleBudget.withIndex().associate { (i, meal) -> meal.name to i }
}

/** Single pass over data: returns per-weekday counts for breakfast, lunch, dinner. */
fun getWeeklyMealCounts(data: List<EatingStatisticsData>): Triple<List<Number>, List<Number>, List<Number>> {
    val counts = Array(3) { IntArray(7) }
    for (d in data) {
        val mealIndex = tokenToIndex[d.tokentype] ?: continue
        counts[mealIndex][d.date.dayOfWeek.value - 1]++
    }
    return Triple(counts[0].asList(), counts[1].asList(), counts[2].asList())
}

/** Single pass over data: returns total counts for breakfast, lunch, dinner. */
fun getMealCounts(data: List<EatingStatisticsData>): Triple<Number, Number, Number> {
    val counts = IntArray(3)
    for (d in data) {
        val mealIndex = tokenToIndex[d.tokentype] ?: continue
        counts[mealIndex]++
    }
    return Triple(counts[0], counts[1], counts[2])
}

/** Single pass over data: returns cumulative daily spending. O(n) instead of O(days * n * meals). */
fun getSpentMoney(
    onBudget: Boolean,
    selectedMonth: String,
    data: List<EatingStatisticsData>,
): List<Number> {
    val daysInMonth = Month.valueOf(selectedMonth.uppercase()).maxLength()
    val meals = MealSample.getMeals(onBudget)
    val priceByToken = meals.associate { it.name to it.price }

    val dailySpending = IntArray(daysInMonth)
    for (d in data) {
        dailySpending[d.date.dayOfMonth - 1] += priceByToken[d.tokentype] ?: 0
    }

    val result = ArrayList<Number>(daysInMonth)
    var sum = 0
    for (i in 0 until daysInMonth) {
        sum += dailySpending[i]
        result.add(sum)
    }
    return result
}
