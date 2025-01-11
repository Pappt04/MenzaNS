package com.pappt04.menzans

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.DummyData.dataweek
import com.pappt04.menzans.DummyData.datetypemonth
import com.pappt04.menzans.DummyData.engmeals
import com.pappt04.menzans.DummyData.engmonths
import com.pappt04.menzans.DummyData.engtosresc
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.Defaults.AXIS_LABEL_ROTATION_DEGREES
import com.patrykandpatrick.vico.core.common.Defaults.COLUMN_ROUNDNESS_PERCENT
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import androidx.compose.runtime.rememberCoroutineScope
import java.util.Locale

@Composable
fun StatisticsScreen(innerpadding: PaddingValues) {
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
                    MealMonthChartColumn(formattedStatisticsData)
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
                    MealWeekChartColumn(formattedStatisticsData)
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
                    PredictedSpendingChart(selectedMonth, formattedStatisticsData)
                }
            }
        }
    }
}


@Composable
fun MealMonthChartColumn(data: List<EatingStatisticsData>) {
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }

    val bottomaxisformatter = CartesianValueFormatter { _, x, _ ->
        MealSample.get(x.toInt() % MealSample.size).name.asString(context)

    }

    val displayBreakfast = getMealNumber(data,Uitext.StringResource(engtosresc(engmeals[0])))
    val displayLunch = getMealNumber(data, Uitext.StringResource(engtosresc(engmeals[1])))
    val displayDinner = getMealNumber(data, Uitext.StringResource(engtosresc(engmeals[2])))

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(
                    displayBreakfast,
                    displayLunch,
                    displayDinner
                )
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 8.dp,
                        shape = CorneredShape.rounded(allPercent = 40),
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                valueFormatter = bottomaxisformatter,
                itemPlacer =
                remember {
                    HorizontalAxis.ItemPlacer.aligned(spacing = 1, addExtremeLabelPadding = true)
                },
            ),
            marker = rememberMarker()
        ),
        modelProducer,
        modifier = Modifier.padding(8.dp)
    )
}

@SuppressLint("RestrictedApi")
@Composable
fun MealWeekChartColumn(data: List<EatingStatisticsData>) {
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }

    val startAxisItemPlacer = VerticalAxis.ItemPlacer.count({ 3 })

    val bottomaxisformatter = CartesianValueFormatter { _, x, _ ->
        dataweek[x.toInt() % 7].asString(context)
    }

    val displayBreakfast = getMealsOnDay(data, Uitext.StringResource(engtosresc(engmeals[0])))
    val displayLunch = getMealsOnDay(data, Uitext.StringResource(engtosresc(engmeals[1])))
    val displayDinner = getMealsOnDay(data, Uitext.StringResource(engtosresc(engmeals[2])))

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries {
                series(displayBreakfast)
                series(displayLunch)
                series(displayDinner)
            }
        }
    }

    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider =
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 4.dp,
                        shape =
                        CorneredShape.rounded(
                            bottomLeftPercent = COLUMN_ROUNDNESS_PERCENT,
                            bottomRightPercent = COLUMN_ROUNDNESS_PERCENT,
                        ),
                    ),
                    rememberLineComponent(
                        color = Color.Yellow,
                        thickness = 4.dp
                    ),
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.tertiary,
                        thickness = 4.dp,
                        shape =
                        CorneredShape.rounded(
                            topLeftPercent = COLUMN_ROUNDNESS_PERCENT,
                            topRightPercent = COLUMN_ROUNDNESS_PERCENT,
                        ),
                    ),
                ),
                mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
            ),
            startAxis =
            VerticalAxis.rememberStart(
                itemPlacer = startAxisItemPlacer,
                labelRotationDegrees = AXIS_LABEL_ROTATION_DEGREES,
            ),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                valueFormatter = bottomaxisformatter,
                labelRotationDegrees = AXIS_LABEL_ROTATION_DEGREES,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
            ),
            marker = rememberMarker(),
            layerPadding =
            cartesianLayerPadding(scalableStartPadding = 16.dp, scalableEndPadding = 16.dp),
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
        modifier = Modifier.padding(8.dp)
    )
}


@Composable
fun PredictedSpendingChart(selectedMonth: String,data: List<EatingStatisticsData>) {
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }


    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    (1..LocalDate.now().month.maxLength()).toList(), getSpentMoney(context,selectedMonth,data)
                )
            }
        }
    }

    val marker = rememberMarker()
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = remember { LineCartesianLayer.LineFill.single(fill(Color(0xffa485e0))) },
                        pointConnector = remember {
                            LineCartesianLayer.PointConnector.cubic(
                                curvature = 0f
                            )
                        },
                    )
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                guideline = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
            ),
            marker = marker,
            layerPadding =
            cartesianLayerPadding(scalableStartPadding = 16.dp, scalableEndPadding = 16.dp),
            persistentMarkers = rememberExtraLambda(marker) { marker at LocalDate.now().dayOfMonth },
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(zoomEnabled = false),
        modifier = Modifier.padding(4.dp)
    )
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
        7,
        { listmeals += 0 }
    )
    for (d in data) {
        if (d.tokentype == token)
            listmeals[d.date.dayOfWeek.value - 1] = listmeals[d.date.dayOfWeek.value - 1] + 1
    }
    return listmeals
}

fun getSpentMoney(context: Context,selectedMonth: String,data: List<EatingStatisticsData>): List<Number> {

    val daysInMonth: Int=Month.valueOf(selectedMonth.uppercase()).maxLength()

    val moneyList = MutableList(daysInMonth) { 0 }
    var sum = 0

    for (i in (1..Month.valueOf(selectedMonth.uppercase()).maxLength())) {
        for (d in data) {
            if (i == d.date.dayOfMonth) {
                var j = 0
                for (e in engmeals) {
                    if (d.tokentype.asString(context) == e) {
                        sum += MealSample[j].price
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