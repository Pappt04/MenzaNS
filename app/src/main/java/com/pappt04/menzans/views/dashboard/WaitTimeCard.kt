package com.pappt04.menzans.views.dashboard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.repository.WaitTimeRepository
import com.pappt04.menzans.views.common.AnimatedNumber
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun WaitTimeCard(refreshTrigger: Int = 0) {
    val waittime = remember { mutableIntStateOf(999) }
    val trajectory = remember { mutableIntStateOf(0) }
    val waitTimeRepository: WaitTimeRepository = koinInject()

    val trendColor = when (trajectory.intValue) {
        -1 -> MaterialTheme.colorScheme.tertiary
        1 -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
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
            trajectory.intValue =
                when {
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { Log.d("WAIT_TIME", "Manual refresh request sent") },
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = stringResource(R.string.wait_time),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (waittime.intValue != 999) {
                    Text(
                        text = "~",
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    AnimatedNumber(
                        number = waittime,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = MaterialTheme.typography.displaySmall,
                    )
                    Text(
                        text = " min",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = if (trajectory.intValue == -1) "Downward trend" else "Upward trend",
                        tint = trendColor,
                        modifier = Modifier.size(28.dp),
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
        }
    }
}
