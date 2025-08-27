package com.pappt04.menzans.appui.info

import TabPickerButton
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import com.pappt04.menzans.appui.animations.AnimatedAppearance
import com.pappt04.menzans.data.consts.UsefulLinks

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