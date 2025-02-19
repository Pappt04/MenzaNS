package com.pappt04.menzans.data

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.pappt04.menzans.R
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

@Composable
fun WaitTimeDisplay(waitTime: MutableIntState, trj: Int) {
    val context= LocalContext.current

    val trajectory= remember { mutableIntStateOf(trj) }

    val textColor = when (trj) {
        -1 -> Color.Green // Green for downward trend
        1 -> Color.Red     // Red for upward trend
        else -> MaterialTheme.colorScheme.secondary // Default color
    }

    val arrowIcon = when (trj) {
        1 -> Icons.Filled.KeyboardArrowUp
        -1 -> Icons.Filled.KeyboardArrowDown
        else -> null // No arrow if trajectory is 0 or other values
    }

    var oldCount by remember {
        mutableIntStateOf(waitTime.intValue)
    }

    SideEffect {
        oldCount = waitTime.intValue
    }
    Row(modifier = Modifier
        .fillMaxHeight()
        .clickable(true) {
            Toast
                .makeText(context, "Refreshing...", Toast.LENGTH_SHORT)
                .show()
            getWaitTime(context) { wt ->
                Log.d("WAIT_TIME", "Refresh request sent")
                if (wt != null) {
                    val temp = waitTime.intValue
                    waitTime.intValue = wt.waittime.toInt()

                    if (temp == 999) {
                        trajectory.intValue = wt.trajectory.toInt()
                    } else if (temp < waitTime.intValue) {
                        trajectory.intValue = 1
                    } else if (temp > waitTime.intValue) {
                        trajectory.intValue = -1
                    } else {
                        trajectory.intValue = 0
                    }
                }
            }
        },
        verticalAlignment = Alignment.CenterVertically) {
        val countString = waitTime.intValue.toString()
        val oldCountString = oldCount.toString()

        Text(stringResource(R.string.wait_time) +":")

        if (waitTime.intValue == 999)
        {
            //Text(stringResource(R.string.no_data))
            Text("? min")
        } else {
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
                ) { ch ->
                    Text(
                        text = ch.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        softWrap = false
                    )
                }
            }

            Text(" min")

            if (arrowIcon != null) {
                Icon(
                    imageVector = arrowIcon,
                    contentDescription = if (trj == -1) "Downward Trend" else "Upward Trend",
                    tint = textColor // Match icon color to text color
                )
            }
        }
    }

}