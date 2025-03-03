package com.pappt04.menzans.appui.navigationdrawer

import androidx.compose.foundation.basicMarquee
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenzaTopBar(firstWelcome: MutableState<Boolean>,drawerState: DrawerState,screenTitle: String) {
    val topbarpopup= remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(colors = topAppBarColors(
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        if (!firstWelcome.value) {
            Text(
                screenTitle,
                softWrap = false,
                fontSize = 32.sp,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee()
            )
        }
    }, navigationIcon = {
        IconButton(onClick = {
            scope.launch {
                drawerState.open()
            }
        }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_description)
            )
        }
    },
        actions = {
            IconButton(
                onClick = {topbarpopup.value = !topbarpopup.value}

            ) {
                Icon(
                    Icons.Default.MoreVert,
                    stringResource(R.string.menu_description)
                )
            }
        }
    )
}
