package com.pappt04.menzans.views.statistics

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.data.consts.CalendarData.weekDays
import com.pappt04.menzans.models.EatingStatisticsData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Defaults.AXIS_LABEL_ROTATION_DEGREES
import com.patrykandpatrick.vico.core.common.Defaults.COLUMN_ROUNDNESS_PERCENT
import com.patrykandpatrick.vico.core.common.shape.CorneredShape

@SuppressLint("RestrictedApi")
@Composable
fun WeeklyMealChart(data: List<EatingStatisticsData>) {
    val context = LocalContext.current
    val modelProducer = remember { CartesianChartModelProducer() }

    val startAxisItemPlacer = VerticalAxis.ItemPlacer.count({ 3 })

    val bottomaxisformatter = CartesianValueFormatter { _, x, _ ->
        weekDays[x.toInt() % 7].asString(context)
    }

    LaunchedEffect(data) {
        val (breakfast, lunch, dinner) = getWeeklyMealCounts(data)
        modelProducer.runTransaction {
            columnSeries {
                series(breakfast)
                series(lunch)
                series(dinner)
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
                        color = MaterialTheme.colorScheme.secondary,
                        thickness = 4.dp,
                        shape =
                        CorneredShape.rounded(
                            bottomLeftPercent = COLUMN_ROUNDNESS_PERCENT,
                            bottomRightPercent = COLUMN_ROUNDNESS_PERCENT,
                        ),
                    ),
                    rememberLineComponent(
                        color = MaterialTheme.colorScheme.primary,
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
