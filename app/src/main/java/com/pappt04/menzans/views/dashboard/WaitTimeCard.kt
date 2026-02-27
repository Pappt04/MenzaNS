package com.pappt04.menzans.views.dashboard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import com.pappt04.menzans.views.common.AnimatedNumber
import com.pappt04.menzans.repository.WaitTimeRepository
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun WaitTimeCard(refreshTrigger: Int = 0) {
    val waittime = remember { mutableIntStateOf(999) }
    val trajectory = remember { mutableIntStateOf(0) }
    val precision = remember { mutableStateOf(0.00) }
    val waitTimeRepository: WaitTimeRepository = koinInject()

    val bscolor = when (trajectory.intValue) {
        -1 -> Color.Green
        1 -> Color.Red
        else -> Color.Gray
    }

    val arrowIcon = when (trajectory.intValue) {
        1 -> Icons.Filled.KeyboardArrowUp
        -1 -> Icons.Filled.KeyboardArrowDown
        else -> Icons.Filled.Remove
    }

    suspend fun fetch() {
        val result = waitTimeRepository.getWaitTime()
        result.onSuccess { wt ->
            val temp = waittime.intValue
            waittime.intValue = wt.waittime?.toIntOrNull() ?: return@onSuccess
            precision.value = wt.density?.toDoubleOrNull() ?: 0.0
            trajectory.intValue = when {
                temp == 999 -> wt.trajectory?.toIntOrNull() ?: 0
                temp < waittime.intValue -> 1
                temp > waittime.intValue -> -1
                else -> 0
            }
        }
    }

    LaunchedEffect(refreshTrigger) {
        Log.d("WAIT_TIME", "Fetch triggered (trigger=$refreshTrigger)")
        fetch()
        while (true) {
            delay(60_000L)
            Log.d("WAIT_TIME", "Auto-refresh request sent")
            fetch()
        }
    }

    Card(
        colors = CardColors(
            MaterialTheme.colorScheme.tertiaryContainer,
            CardDefaults.cardColors().contentColor,
            CardDefaults.cardColors().disabledContainerColor,
            CardDefaults.cardColors().disabledContentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(8.dp)
            .clickable { Log.d("WAIT_TIME", "Manual refresh request sent") },
    ) {
        Column {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.wait_time),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    if (waittime.intValue != 999) {
                        Text(
                            text = "~",
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        AnimatedNumber(
                            waittime,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(2.dp)
                        )
                    } else {
                        Text(
                            text = "?",
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                    Text(
                        text = " min",
                        fontSize = 40.sp,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(2.dp)
                    )
                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = if (trajectory.intValue == -1) "Downward Trend" else "Upward Trend",
                        tint = bscolor
                    )
                }
                Text(
                    stringResource(R.string.precision, precision.value + 10),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
