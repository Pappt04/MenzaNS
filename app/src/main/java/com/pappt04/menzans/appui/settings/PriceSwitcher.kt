package com.pappt04.menzans.appui.settings

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.R
import com.pappt04.menzans.appui.animations.AutoResizedText
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.FileContainer
import com.pappt04.menzans.data.FileDAO

@Composable
fun PriceSwitcher(onBudget: MutableState<Boolean>) {
    val context= LocalContext.current
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

                val dark= if(onBudget.value) 1 else 0

                val fdao= FileDAO(context,FileContainer.FileMealPricing)
                fdao.saveToFile(dark,false)
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}