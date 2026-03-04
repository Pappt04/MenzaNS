package com.pappt04.menzans.views.info

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.pappt04.menzans.R
import com.pappt04.menzans.ui.theme.IconSize
import com.pappt04.menzans.ui.theme.Spacing

private data class TabDef(val icon: ImageVector, val labelRes: Int)

private val TABS = listOf(
    TabDef(Icons.Outlined.Restaurant, R.string.menu_description),
    TabDef(Icons.Outlined.Link, R.string.links),
)

@Composable
fun TabPickerButton(globaltab: MutableState<Int>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(modifier = Modifier.padding(Spacing.xs)) {
            TABS.forEachIndexed { index, tab ->
                val selected = globaltab.value == index
                val bgColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.tertiary
                                  else Color.Transparent,
                    label = "tabBg$index",
                )
                val contentColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.onTertiary
                                  else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "tabContent$index",
                )
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { globaltab.value = index },
                    shape = MaterialTheme.shapes.extraLarge,
                    color = bgColor,
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = Spacing.sm),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(IconSize.medium),
                        )
                        Spacer(Modifier.width(Spacing.xs))
                        Text(
                            text = stringResource(tab.labelRes),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor,
                        )
                    }
                }
            }
        }
    }
}
