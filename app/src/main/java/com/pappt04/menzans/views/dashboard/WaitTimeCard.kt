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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.ui.theme.Spacing
import com.pappt04.menzans.repository.WaitTimeRepository
import com.pappt04.menzans.views.common.AnimatedNumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private const val WAIT_TIME_LOADING = 999
private const val WAIT_TIME_ERROR = -1

@Composable
fun WaitTimeCard(refreshTrigger: Int = 0, snackbar: SnackbarHostState? = null) {
    val waittime = remember { mutableIntStateOf(WAIT_TIME_LOADING) }
    val trajectory = remember { mutableIntStateOf(0) }
    val hasError = remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val waitTimeRepository: WaitTimeRepository = koinInject()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
        if (result.isFailure) {
            hasError.value = true
            return
        }
        result.onSuccess { wt ->
            val parsed = wt.waittime?.toIntOrNull()
            if (parsed == null) {
                hasError.value = true
                return@onSuccess
            }
            hasError.value = false
            val temp = waittime.intValue
            waittime.intValue = parsed
            trajectory.intValue =
                when {
                    temp == WAIT_TIME_LOADING -> wt.trajectory?.toIntOrNull() ?: 0
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
            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
            .clickable { if (!hasError.value) showDialog.value = true },
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
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
                when {
                    hasError.value -> {
                        Text(
                            text = stringResource(R.string.wait_time_unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f),
                        )
                    }
                    waittime.intValue != WAIT_TIME_LOADING -> {
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
                            text = " ${stringResource(R.string.min_unit)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        Icon(
                            imageVector = arrowIcon,
                            contentDescription = if (trajectory.intValue == -1) stringResource(R.string.trend_downward) else stringResource(R.string.trend_upward),
                            tint = trendColor,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    else -> {
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

    if (showDialog.value) {
        WaitTimeDialog(
            onDismissRequest = { showDialog.value = false },
            onSubmitted = { success ->
                scope.launch {
                    val message = if (success) {
                        context.getString(R.string.wait_time_submitted)
                    } else {
                        context.getString(R.string.wait_time_submit_failed)
                    }
                    snackbar?.showSnackbar(message)
                }
            },
        )
    }
}
