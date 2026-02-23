package com.pappt04.menzans.views.common


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AnimatedAppearance(
    delay: Duration= 0.milliseconds,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn() + expandIn(),
    label: String = "AnimatedAppearance",
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    var isVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(delay) {
        delay(delay)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = enter,
        label = label,
        content = content,
    )
}
