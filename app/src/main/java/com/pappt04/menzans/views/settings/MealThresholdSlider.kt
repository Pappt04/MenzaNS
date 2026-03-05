package com.pappt04.menzans.views.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.views.common.AutoResizedText
import kotlin.math.roundToInt

@Composable
fun MealThresholdSlider(
    label: String,
    sliderpos: MutableFloatState,
    onChanged: () -> Unit,
) {
    val valueStyle = MaterialTheme.typography.titleMedium.merge(
        TextStyle(
            lineHeight = 2.5.em,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Row {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle = PlatformTextStyle(includeFontPadding = false),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None,
                        ),
                    ),
                ),
                modifier = Modifier.weight(4f),
            )
            AutoResizedText(
                text = "${sliderpos.floatValue.roundToInt()}%",
                style = valueStyle,
                modifier = Modifier.weight(1f),
            )
        }
        Slider(
            modifier = Modifier.semantics { contentDescription = label },
            value = sliderpos.floatValue,
            onValueChange = { sliderpos.floatValue = it },
            valueRange = 10f..100f,
            steps = 17,
            onValueChangeFinished = { onChanged() },
        )
    }
}
