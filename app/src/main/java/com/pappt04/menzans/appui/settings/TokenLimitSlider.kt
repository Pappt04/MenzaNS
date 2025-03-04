package com.pappt04.menzans.appui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.R
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.FileContainer
import com.pappt04.menzans.data.FileDAO
import kotlin.math.roundToInt


@Preview
@Composable
fun TokenLimitSlider() {
    val context = LocalContext.current

    var sliderPosition = remember { mutableStateOf(DummyData.MINIMUM_TOKEN_TRESHOLD.toFloat()) }

    try {
        val dao = FileDAO(context, FileContainer.FileTokenLimit)
        val tokenlimitstring = dao.readFromFile()
        sliderPosition.value=tokenlimitstring.toFloat()
    } catch (_:Exception) {}

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
                "${sliderPosition.value.roundToInt()}",
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
            value = sliderPosition.value,
            onValueChange = { sliderPosition.value = it },
            valueRange = 0f..10f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
                val fdao = FileDAO(context, FileContainer.FileTokenLimit)
                fdao.saveToFile(sliderPosition.value.roundToInt(), false)

            },
            steps = 9
        )
    }
}