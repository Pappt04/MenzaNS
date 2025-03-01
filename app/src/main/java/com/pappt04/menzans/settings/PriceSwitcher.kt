package com.pappt04.menzans.settings

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.R
import com.pappt04.menzans.SettingsDataStorage
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.FileDAO
import com.pappt04.menzans.data.settingsdatastorage.SettingsPreferenceKeys
import com.pappt04.menzans.data.settingsdatastorage.updateBoolean

@Composable
fun PriceSwitcher(context: Context, onBudget: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                stringResource(R.string.self_financing),
                style = LocalTextStyle.current.merge(
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
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            Switch(
                checked = onBudget.value,
                onCheckedChange = {
                    onBudget.value = it
                    var bgt=0
                    bgt = if(onBudget.value) {
                        1
                    } else {
                        0
                    }
                    val fdao= FileDAO(context, DummyData.FileMealPricing)
                    fdao.saveToFile(bgt,false)

                    //SettingsDataStorage.updateBoolean(onBudget.value,DummyData.FINANCING_KEY,SettingsPreferenceKeys.FINANCING_PREFERENCE)
                },
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
            )
            Text(
                stringResource(R.string.budget),
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}