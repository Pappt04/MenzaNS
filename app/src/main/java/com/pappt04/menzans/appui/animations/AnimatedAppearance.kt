package com.pappt04.menzans.appui.animations


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

@Composable
fun AnimatedAppearance(
    delay: Duration,
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
