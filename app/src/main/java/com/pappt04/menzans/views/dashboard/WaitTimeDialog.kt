package com.pappt04.menzans.views.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pappt04.menzans.R
import com.pappt04.menzans.repository.UserRepository
import com.pappt04.menzans.repository.WaitTimeRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun WaitTimeDialog(
    onDismissRequest: () -> Unit,
    onSubmitted: (success: Boolean) -> Unit,
) {
    var waitTimeText by remember { mutableStateOf("") }
    var queueSizeText by remember { mutableStateOf("") }
    val waitTimeRepository: WaitTimeRepository = koinInject()
    val userRepository: UserRepository = koinInject()
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
                Text(
                    text = stringResource(R.string.report_wait_time),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                OutlinedTextField(
                    value = waitTimeText,
                    onValueChange = { waitTimeText = it },
                    label = { Text(stringResource(R.string.wait_time_minutes_optional)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = queueSizeText,
                    onValueChange = { queueSizeText = it },
                    label = { Text(stringResource(R.string.queue_size_optional)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.dismiss))
                    }
                    val time = waitTimeText.toIntOrNull()
                    val queueSize = queueSizeText.toIntOrNull()
                    Button(
                        onClick = {
                            val userId = userRepository.getUserId()
                            scope.launch {
                                val result = waitTimeRepository.submitWaitTime(userId, time, queueSize)
                                onDismissRequest()
                                onSubmitted(result.isSuccess)
                            }
                        },
                        enabled = time != null || queueSize != null,
                    ) {
                        Text(stringResource(R.string.submit))
                    }
                }
            }
        }
    }
}
