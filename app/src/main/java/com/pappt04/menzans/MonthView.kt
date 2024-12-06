package com.pappt04.menzans

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.DummyData.datetypemonth
import com.pappt04.menzans.DummyData.engmeals
import com.pappt04.menzans.DummyData.engmonths
import java.util.Date

@Composable
fun MonthView(data: List<EatingStatisticsData>) {
    val context = LocalContext.current

    var currentlySelected = remember { mutableIntStateOf(0) }

    var datelist = mutableListOf<Int>()

    var showDialog = remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { currentlySelected.intValue = 0 }
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                stringResource(R.string.collected_data_from_current_month),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            for (i in (1..31)) {
                datelist += i
                if (i % 7 == 0) {
                    WeekHelper(days = datelist, data = data, currentlySelected)
                    datelist = emptyList<Int>().toMutableList()
                }
            }
            while (datelist.size != 7)
                datelist += 0
            if (datelist.size == 7) {
                WeekHelper(datelist, data, currentlySelected)
                datelist = emptyList<Int>().toMutableList()
            }

            Spacer(modifier = Modifier.padding(5.dp))

            AnimatedVisibility(
                currentlySelected.intValue != 0,
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(10.dp))
                    ShowStatisticsDayData(selected = currentlySelected, data)
                    Button(
                        onClick = {
                            showDialog.value = true
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                    ) {
                        Text("Add meal")
                    }
                    if (showDialog.value)
                        AddMealDialog(
                            onDismissRequest = {
                                showDialog.value = false
                            },
                            context,
                            currentlySelected.intValue
                        )
                }
            }
        }
    }

}

@Composable
fun WeekHelper(days: List<Int>, data: List<EatingStatisticsData>, selected: MutableState<Int>) {
    LazyRow(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(days) { day: Int ->
            var i = 0
            for (meal in data) {
                if (day == meal.date.dayOfMonth)
                    i++
            }
            if (day != 0) {
                DayView(day.toString(), i, selected)
            } else {
                EmptyDayView()
            }
        }
    }
}

@Composable
fun ShowStatisticsDayData(selected: MutableState<Int>, data: List<EatingStatisticsData>) {
    val context= LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        for (meal in data) {
            if (meal.date.dayOfMonth == selected.value) {
                var i = 0
                for (e in engmeals) {
                    if (meal.tokentype.asString(context) == e)
                        break
                    i++
                }
                val enteredsplit = meal.timeentered.split(":").toTypedArray()
                val exitedsplit = meal.timeexited.split(":").toTypedArray()

                val str = "${meal.timeentered}-${meal.timeexited} \t ${
                    MealSample[i].name.asString(
                        LocalContext.current
                    )
                }"
                Row() {
                    OutlinedTextField(
                        value = str,
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        ),
                        suffix = {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .clickable {
                                        var sdao = StatisticsFileDAO(
                                            context,
                                            data[0].date.month.value.toString()
                                        )
                                        sdao.removeFromStatistics(data, meal)
                                    }
                            )
                        },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(4f)
                    )

                }
            }
        }
    }
}