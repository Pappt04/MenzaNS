package com.pappt04.menzans.views.statistics

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.models.EatingStatisticsData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.time.LocalDate
import java.time.Month
import java.util.Locale


@Composable
fun PredictedSpendingChart(selectedMonth: String, onBudget: MutableState<Boolean>, data: List<EatingStatisticsData>) {
    val modelProducer = remember { CartesianChartModelProducer() }


    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    (1..Month.valueOf(selectedMonth.uppercase(Locale.ROOT)).maxLength()).toList(), getSpentMoney(
                        onBudget,
                        selectedMonth,
                        data
                    )
                )
            }
        }
    }

    val c = MaterialTheme.colorScheme.tertiary
    val marker = rememberMarker()
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = remember { LineCartesianLayer.LineFill.single(fill(c)) },
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
