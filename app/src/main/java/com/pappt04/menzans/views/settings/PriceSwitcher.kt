package com.pappt04.menzans.views.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.R

@Composable
fun PriceSwitcher(onBudget: MutableState<Boolean>, onChanged: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            text=when(onBudget.value){
                true -> stringResource(R.string.budget)
                else -> stringResource(R.string.self_financing)
            },
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
            modifier = Modifier
                .weight(4f)
        )
        Switch(
            checked = onBudget.value,
            onCheckedChange = {
                onBudget.value = it

                onChanged()
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}