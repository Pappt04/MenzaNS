package com.pappt04.menzans.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.pappt04.menzans.data.Uitext


@Composable
fun AnimatedWord(cword: MutableState<Uitext>) {

    val str= cword.value.asString(LocalContext.current)

    val word= remember { mutableStateOf(str) }

    var oldCount by remember {
        mutableStateOf(word.value)
    }
    SideEffect {
        oldCount = word.value
    }
    Row() {
        val countString = word.value
        val oldCountString = oldCount

        for(i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if(oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            ) { chr ->
                Text(
                    text = chr.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    softWrap = false
                )
            }
        }
    }
}