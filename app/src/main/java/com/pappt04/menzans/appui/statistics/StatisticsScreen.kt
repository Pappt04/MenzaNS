package com.pappt04.menzans.appui.statistics

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.data.DummyData.datetypemonth
import com.pappt04.menzans.data.DummyData.engmeals
import com.pappt04.menzans.data.DummyData.engmonths
import java.time.Month
import java.time.format.TextStyle
import java.util.Date
import androidx.compose.ui.tooling.preview.Preview
import com.pappt04.menzans.data.EatingStatisticsData
import com.pappt04.menzans.R
import com.pappt04.menzans.data.MealSample.MealSampleBudget
import com.pappt04.menzans.data.MealSample.MealSampleSelfFinancing
import com.pappt04.menzans.data.StatisticsFileDAO
import com.pappt04.menzans.data.Uitext
import com.pappt04.menzans.geolocation.findEngMeal
import java.util.Locale

@Composable
fun StatisticsScreen(innerpadding: PaddingValues, onBudget: MutableState<Boolean>) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var selectedMonth by remember { mutableStateOf(engmonths[datetypemonth.format(Date()).toInt()-1]) }

    val monthDAO= StatisticsFileDAO(context, selectedMonth)
    var formattedStatisticsData = monthDAO.getStatisticsData()


    key(formattedStatisticsData){
        LazyColumn(
            modifier = Modifier
                .padding(innerpadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(engmonths) { month ->
                        val localizedMonth = Month.valueOf(month.uppercase()).getDisplayName(
                            TextStyle.SHORT,
                            Locale.getDefault()
                        )

                        FilterChip(
                            onClick = {
                                selectedMonth = if (selectedMonth == month) {
                                    engmonths[datetypemonth.format(Date()).toInt() - 1]
                                } else {
                                    month
                                }
                                scope.launch{
                                    monthDAO.changeJob(context,selectedMonth)
                                    formattedStatisticsData= monthDAO.getStatisticsData()
                                }
                            },
                            label = { Text(localizedMonth) },
                            selected = selectedMonth == month
                        )
                    }
                }
            }

            item {
                CalendarMonthView(selectedMonth,formattedStatisticsData)
            }

            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                )
                {
                    Text(
                        stringResource(R.string.your_monthly_token_usage),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    MonthlyMealsChart(formattedStatisticsData)
                }
            }
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                )
                {
                    Text(
                        stringResource(R.string.your_weekly_token_usage),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    WeeklyMealChart(formattedStatisticsData)
                }
            }
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .padding(8.dp)
                )
                {
                    Text(
                        stringResource(R.string.predicted_spending),
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    PredictedSpendingChart(selectedMonth, onBudget,formattedStatisticsData)
                }
            }
        }
    }
}


fun getMealNumber(data: List<EatingStatisticsData>, token: Uitext): Number {
    var i = 0
    for (d in data) {
        if (d.tokentype == token)
            i++
    }
    return i
}

fun getMealsOnDay(data: List<EatingStatisticsData>, token: Uitext): List<Number> {
    val listmeals = mutableListOf<Int>()
    repeat(
        7
    ) { listmeals += 0 }
    for (d in data) {
        if (d.tokentype == token)
            listmeals[d.date.dayOfWeek.value - 1] = listmeals[d.date.dayOfWeek.value - 1] + 1
    }
    return listmeals
}

fun getSpentMoney(
    onBudget: MutableState<Boolean>,
    selectedMonth: String,
    data: List<EatingStatisticsData>
): List<Number> {

    val daysInMonth: Int=Month.valueOf(selectedMonth.uppercase()).maxLength()

    val moneyList = MutableList(daysInMonth) { 0 }
    var sum = 0

    for (i in (1..Month.valueOf(selectedMonth.uppercase()).maxLength())) {
        for (d in data) {
            if (i == d.date.dayOfMonth) {
                var j = 0
                for (e in engmeals) {
                    if (findEngMeal(d.tokentype) == e) {
                        sum += when(onBudget.value){
                            true -> MealSampleBudget[j].price
                            else -> MealSampleSelfFinancing[j].price
                        }
                        break
                    }
                    j++
                }
            }
        }
        moneyList[i-1] = sum
    }

    return moneyList
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun StatisticsScreenPreview() {

    val b= remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Statistics Screen Preview") })
        }
    ) { innerPadding ->
        StatisticsScreen(innerPadding,b)
    }
}