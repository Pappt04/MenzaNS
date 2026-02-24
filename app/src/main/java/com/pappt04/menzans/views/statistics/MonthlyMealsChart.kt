package com.pappt04.menzans.views.statistics

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.CorneredShape

@Composable
fun MonthlyMealsChart(data: List<EatingStatisticsData>) {
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }

    val bottomaxisformatter = CartesianValueFormatter { _, x, _ ->
        MealSampleBudget[x.toInt() % MealSampleBudget.size].name.asString(context)

    }

    LaunchedEffect(data) {
        val (breakfast, lunch, dinner) = getMealCounts(data)
        modelProducer.runTransaction {
            columnSeries {
                series(breakfast, lunch, dinner)
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
