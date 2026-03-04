package com.pappt04.menzans.views.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ShowChart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.models.UiState
import com.pappt04.menzans.viewmodels.DashboardViewModel
import com.pappt04.menzans.views.statistics.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun LineGraphCard(viewModel: DashboardViewModel) {
    val graphState by viewModel.graphState.collectAsState()
    val currentMeal by viewModel.currentMeal.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchGraphData()
    }

    val mealLabelRes = when (currentMeal) {
        "breakfast" -> R.string.breakfast
        "lunch" -> R.string.lunch
        "dinner" -> R.string.dinner
        else -> null
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ShowChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
                Column {
                    Text(
                        text = stringResource(R.string.busyness_forecast),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    if (mealLabelRes != null) {
                        Text(
                            text = stringResource(mealLabelRes),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            when (val state = graphState) {
                is UiState.Loading, is UiState.Empty -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
                    }
                }
                is UiState.Success -> {
                    val data = state.data
                    // Parse and sort once; pass down to both metrics and chart
                    val sortedEntries = remember(data) {
                        data.entries
                            .sortedBy { it.key }
                            .map { (k, v) -> parseTimeToDecimalHour(k) to v }
                    }
                    val nowDecimal = remember {
                        LocalTime.now().let { t -> t.hour + t.minute / 60.0 }
                    }
                    // Integer index of the data point nearest to the current time,
                    // or null when the current time is outside the meal window.
                    val nowIndex: Int? = remember(sortedEntries) {
                        if (sortedEntries.isEmpty()) return@remember null
                        val inRange = nowDecimal >= sortedEntries.first().first &&
                            nowDecimal <= sortedEntries.last().first
                        if (!inRange) return@remember null
                        sortedEntries.indexOfFirst { (h, _) -> h >= nowDecimal }
                            .takeIf { it >= 0 }
                    }
                    val currentBusyness = nowIndex?.let { sortedEntries.getOrNull(it)?.second }
                    val peakEntry = remember(sortedEntries) {
                        sortedEntries.maxByOrNull { it.second }
                    }

                    // Metric chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (currentBusyness != null) {
                            BusynessMetric(
                                label = stringResource(R.string.metric_now),
                                value = "${currentBusyness.roundToInt()}%",
                                modifier = Modifier.weight(1f),
                            )
                        }
                        if (peakEntry != null) {
                            val pH = peakEntry.first.toInt()
                            val pM = ((peakEntry.first - pH) * 60).roundToInt()
                            BusynessMetric(
                                label = stringResource(R.string.metric_peak),
                                value = "%d%% · %02d:%02d".format(peakEntry.second.roundToInt(), pH, pM),
                                modifier = if (currentBusyness != null) Modifier.weight(1f)
                                           else Modifier.fillMaxWidth(0.5f),
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    LineSizeGraph(sortedEntries = sortedEntries, nowIndex = nowIndex)
                }
                is UiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BusynessMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/**
 * Renders the busyness line chart for a single meal window.
 *
 * @param sortedEntries List of (decimalHour, busyness 0-100) sorted by time.
 * @param nowIndex      Integer index into [sortedEntries] for the current time,
 *                      or null when the current time is outside the window.
 */
@Composable
fun LineSizeGraph(
    sortedEntries: List<Pair<Double, Double>>,
    nowIndex: Int? = null,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    // Use sequential integer indices as x-values so Vico's GCD is always 1,
    // avoiding the "too precise" crash and giving clean spacing arithmetic.
    LaunchedEffect(sortedEntries) {
        if (sortedEntries.isEmpty()) return@LaunchedEffect
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = sortedEntries.indices.map { it.toDouble() },
                    y = sortedEntries.map { it.second },
                )
            }
        }
    }

    val lineColor = MaterialTheme.colorScheme.primary
    val areaColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    val marker = rememberMarker()

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = remember(lineColor) {
                            LineCartesianLayer.LineFill.single(fill(lineColor))
                        },
                        areaFill = remember(areaColor) {
                            LineCartesianLayer.AreaFill.single(fill(areaColor))
                        },
                        pointConnector = remember {
                            LineCartesianLayer.PointConnector.cubic(curvature = 0.3f)
                        },
                    )
                )
            ),
            // Y-axis: show busyness as a percentage
            startAxis = VerticalAxis.rememberStart(
                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurface),
                valueFormatter = remember {
                    CartesianValueFormatter { _, value, _ -> "${value.toInt()}%" }
                },
            ),
            // X-axis: map integer index → "HH:MM", label only at 15-minute boundaries.
            // spacing=3 steps through every 3rd index (= every 15 min for 5-min data).
            bottomAxis = HorizontalAxis.rememberBottom(
                label = rememberTextComponent(color = MaterialTheme.colorScheme.onSurface),
                valueFormatter = remember(sortedEntries) {
                    CartesianValueFormatter { _, value, _ ->
                        val idx = value.roundToInt()
                        val decHour = sortedEntries.getOrNull(idx)?.first
                            ?: return@CartesianValueFormatter "–"
                        val h = decHour.toInt()
                        val m = ((decHour - h) * 60).roundToInt()
                        "%02d:%02d".format(h, m)
                    }
                },
                itemPlacer = remember {
                    HorizontalAxis.ItemPlacer.aligned(
                        spacing = 3,
                        addExtremeLabelPadding = true,
                    )
                },
            ),
            marker = marker,
            persistentMarkers = rememberExtraLambda(marker) {
                if (nowIndex != null) marker at nowIndex
            },
            layerPadding = cartesianLayerPadding(
                scalableStartPadding = 8.dp,
                scalableEndPadding = 8.dp,
            ),
        ),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        zoomState = rememberVicoZoomState(initialZoom = Zoom.Content),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
    )
}

/** Parses "HH:MM" into a decimal hour rounded to 4 decimal places (Vico's maximum precision). */
fun parseTimeToDecimalHour(timeStr: String): Double {
    val parts = timeStr.split(":")
    val h = parts[0].toInt()
    val m = parts[1].toInt()
    val raw = h + m / 60.0
    return (raw * 10000).roundToInt() / 10000.0
}
