package com.pappt04.menzans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StatisticsScreen(innerpadding: PaddingValues) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .padding(innerpadding)){
        Text(
            "Coming Soon...",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}
