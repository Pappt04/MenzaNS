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
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.pappt04.menzans.data.consts.CalendarData.weekDays
import com.pappt04.menzans.models.EatingStatisticsData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Defaults.AXIS_LABEL_ROTATION_DEGREES
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
                        fill = fill(MaterialTheme.colorScheme.secondary),
                        thickness = 4.dp,
                        shape =
                        CorneredShape.rounded(
                            bottomLeftPercent = 40,
                            bottomRightPercent = 40,
                        ),
                    ),
                    rememberLineComponent(
                        fill = fill(MaterialTheme.colorScheme.primary),
                        thickness = 4.dp
                    ),
                    rememberLineComponent(
                        fill = fill(MaterialTheme.colorScheme.tertiary),
                        thickness = 4.dp,
                        shape =
                        CorneredShape.rounded(
                            topLeftPercent = 40,
                            topRightPercent = 40,
                        ),
                    ),
                ),
                mergeMode = { ColumnCartesianLayer.MergeMode.Stacked },
            ),
            startAxis =
            VerticalAxis.rememberStart(
                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurface),
                itemPlacer = startAxisItemPlacer,
                labelRotationDegrees = AXIS_LABEL_ROTATION_DEGREES,
            ),
            bottomAxis =
            HorizontalAxis.rememberBottom(
                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurface),
                valueFormatter = bottomaxisformatter,
                labelRotationDegrees = AXIS_LABEL_ROTATION_DEGREES,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
            ),
            marker = rememberMarker(),
            layerPadding = { cartesianLayerPadding() },
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        zoomState = rememberVicoZoomState(zoomEnabled = false),
        modifier = Modifier.padding(8.dp)
    )
}
