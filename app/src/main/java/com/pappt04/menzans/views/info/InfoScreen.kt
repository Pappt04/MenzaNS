package com.pappt04.menzans.views.info

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.pappt04.menzans.viewmodels.MenuViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(innerpadding: PaddingValues) {
    val tab = remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val menuViewModel: MenuViewModel = koinViewModel()
    val isMenuLoading by menuViewModel.weekLoading.collectAsState()

    val isRefreshing = tab.intValue == 0 && isMenuLoading
    val onRefresh: () -> Unit = {
        if (tab.intValue == 0) menuViewModel.refreshWeekMenu()
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.padding(innerpadding),
    ) {
        LazyColumn {
            item {
                TabPickerButton(tab)
            }
            when (tab.intValue) {
                0 -> item {
                    MenuTab()
                }
                else -> item {
                    LinkTab(context)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode",
)
@Composable
fun InfoScreenPreview() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Statistics Screen Preview") })
        },
    ) { innerPadding ->
        InfoScreen(innerPadding)
    }
}
