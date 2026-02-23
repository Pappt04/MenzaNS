package com.pappt04.menzans.views.info

import android.content.res.Configuration
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.pappt04.menzans.views.common.AnimatedAppearance

@Composable
fun InfoScreen(innerpadding: PaddingValues) {
    val context = LocalContext.current

    val tab = remember { mutableIntStateOf(1) }

    LazyColumn(modifier = Modifier.padding(innerpadding)) {
//        item{
//            TabPickerButton(tab)
//        }
        when (tab.intValue) {
            0 -> item{
                AnimatedAppearance(
                    enter = slideInHorizontally()
                ) {
                    MenuTab()
                }
            }
            else -> item{
//                AnimatedAppearance(
//                    enter = slideInHorizontally(initialOffsetX = {it/2})
//                ) {
//
//                    LinkTab(context)
//                }
                LinkTab(context)
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun InfoScreenPreview() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Statistics Screen Preview") })
        }
    ) { innerPadding ->
        InfoScreen(innerPadding)
    }
}