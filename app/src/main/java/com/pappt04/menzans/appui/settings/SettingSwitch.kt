package com.pappt04.menzans.appui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.pappt04.menzans.data.FileDAO

@Composable
fun SettingSwitch(pref: MutableState<Boolean>, name: String, filename: String, onChanged: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Text(
            name,
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
            checked = pref.value,
            onCheckedChange = {
                pref.value = it

                onChanged()
//                val dark = if (pref.value) 1 else 0
//
//                val fdao = FileDAO(context, filename)
//                fdao.saveToFile(dark, false)
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}
