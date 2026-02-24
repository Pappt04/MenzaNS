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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.R
import kotlin.math.roundToInt


@Composable
fun TokenLimitSlider(sliderpos: MutableFloatState, onChanged: () -> Unit) {
    LocalContext.current

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row()
        {
            Text(
                stringResource(R.string.token_warning),
                style = MaterialTheme.typography.titleLarge.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ),
                modifier = Modifier.weight(4f)
            )
            Text(
                "${sliderpos.value.roundToInt()}",
                style = MaterialTheme.typography.titleLarge.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ), modifier = Modifier.weight(1f)
            )
        }
        Slider(
            modifier = Modifier.semantics { contentDescription = "Localized Description" },
            value = sliderpos.value,
            onValueChange = { sliderpos.value = it },
            valueRange = 0f..10f,
            onValueChangeFinished = {
                onChanged()
            },
            steps = 9
        )
    }
}