package com.pappt04.menzans.views.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.os.LocaleListCompat
import com.pappt04.menzans.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChanger() {
    var isExpanded by remember { mutableStateOf(false) }

    val localeOptions =
        mapOf(
            R.string.en to "en",
            R.string.hu to "hu",
            R.string.sr to "sr",
        ).mapKeys { stringResource(it.key) }

    Row(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            stringResource(R.string.change_your_language),
            style =
                MaterialTheme.typography.titleLarge.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        lineHeightStyle =
                            LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None,
                            ),
                    ),
                ),
            modifier =
                Modifier
                    .weight(3f),
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded },
            modifier =
                Modifier
                    .weight(2f),
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = stringResource(R.string.language),
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                localeOptions.keys.forEach { selectionLocale ->
                    DropdownMenuItem(
                        onClick = {
                            isExpanded = false
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(
                                    localeOptions[selectionLocale],
                                ),
                            )
                        },
                        text = { Text(selectionLocale) },
                    )
                }
            }
        }
    }
}
