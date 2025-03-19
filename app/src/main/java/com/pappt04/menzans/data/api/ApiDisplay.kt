package com.pappt04.menzans.data.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay


@Composable
fun MinuteTicker(onTick: () -> Unit) {
    LaunchedEffect(Unit) {
        while (true) {
            onTick()
            delay(60_000L) // Delay for 60 seconds
        }
    }
}
