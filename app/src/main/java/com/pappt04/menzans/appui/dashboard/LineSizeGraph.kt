package com.pappt04.menzans.appui.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.pappt04.menzans.R
import com.pappt04.menzans.data.getLineGraph
import com.pappt04.menzans.appui.statistics.rememberMarker
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds


sealed class UiState {
    object Empty : UiState()
    object Loading : UiState()
    data class Success(val data: Map<String, Double>) : UiState()
    data class Error(val message: String) : UiState()
}

class GraphCardViewModel : androidx.lifecycle.ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // Set loading state
            delay(2000.milliseconds)
            try {
                getLineGraph { d ->
                    if (d != null) _uiState.value = UiState.Success(d)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An error occurred") // Set error state
            }
        }
    }
}

@Composable
fun LineSizeGraph(linemap: Map<String, Double>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val context= LocalContext.current

    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    convertMapKeysToList(linemap), linemap.values
                )
            }
        }
    }

    val c = MaterialTheme.colorScheme.secondary

    val marker = rememberMarker()
    CartesianChartHost(
        chart = rememberCartesianChart(
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
            startAxis = VerticalAxis.rememberStart(
                titleComponent =
                rememberTextComponent(
                    color = c,
                    margins = dimensions(top = 4.dp),
                    padding = dimensions(8.dp, 2.dp),

                ),
                title = stringResource(R.string.wait_time_min)
            ),
            bottomAxis =  HorizontalAxis.rememberBottom(
                valueFormatter = remember { CartesianValueFormatter { context, value, verticalAxisPosition ->
                   convertTimeValuetoString(value,verticalAxisPosition)
                } },
                itemPlacer =
                remember { HorizontalAxis.ItemPlacer.aligned(addExtremeLabelPadding = true) },
                titleComponent =
                rememberTextComponent(
                    color = c,
                    margins = dimensions(top = 4.dp),
                    padding = dimensions(8.dp, 2.dp),
                    background = shapeComponent(MaterialTheme.colorScheme.tertiaryContainer, CorneredShape.Pill),
                ),
                title = stringResource(R.string.time_of_day_hours),
            ),
            marker = marker,
            layerPadding = cartesianLayerPadding(
                scalableStartPadding = 16.dp,
                scalableEndPadding = 16.dp
            ),
            persistentMarkers = rememberExtraLambda(marker) { marker at LocalTime.now().hour+(LocalTime.now().minute/60) },
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(initialZoom = Zoom.Content),
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        modifier = Modifier.padding(4.dp)
    )
}


fun convertMapKeysToList(timeMap: Map<String, Double>): List<Double> {
    val timeList = mutableListOf<Double>()

    for (hour in 0..23) {
        for (minute in 0..55 step 5) {
            val fractionalHour = hour + minute / 60.0
            //timeList.add(fractionalHour)
            timeList.add(String.format(locale = Locale.ROOT,"%.2f", fractionalHour).toDouble())
        }
    }

    timeList.add(24.0)

    return timeList
}

fun convertTimeValuetoString(time: Double, vertipost: Axis.Position.Vertical?): String {

    var decimal = time.roundToInt()
    var fraction= time-decimal
    fraction=fraction.absoluteValue

    var s= String.format(Locale.ROOT,"%d:%2.2f",decimal,fraction)

    var f="$decimal:$fraction"

    return "$decimal:00"
}