package com.pappt04.menzans.views.statistics

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pappt04.menzans.data.consts.CalendarData.mealNames
import com.pappt04.menzans.data.consts.CalendarData.mealNameToRes
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.R
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.views.card.convertMillisToDate
import com.pappt04.menzans.data.consts.MealSample.MealSampleBudget
import com.pappt04.menzans.viewmodels.StatisticsViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(
    onDismissRequest: () -> Unit,
    context: Context,
    day: MutableState<LocalDate>,
    viewModel: StatisticsViewModel,
    monthName: String
) {
    val timeofEnter = remember { mutableStateOf("") }
    val showEnterDialog = remember { mutableStateOf(false) }
    val enterPickerState = rememberTimePickerState(0, 0, true)

    val timeofExit = remember { mutableStateOf("") }
    val showExitDialog = remember { mutableStateOf(false) }
    val exitPickerState = rememberTimePickerState(0, 0, true)

    var selectedMeal by remember { mutableStateOf(Uitext.StringResource(R.string.breakfast)) }
    var isExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    if (showEnterDialog.value) {
        TimePickerDialog(
            onDismiss = { showEnterDialog.value = false },
            timeState = enterPickerState,
            enteredtime = timeofEnter,
        ) {
            TimePicker(state = enterPickerState)
        }
    }
    if (showExitDialog.value) {
        TimePickerDialog(
            onDismiss = { showExitDialog.value = false },
            timeState = exitPickerState,
            enteredtime = timeofExit,
        ) {
            TimePicker(state = exitPickerState)
        }
    }

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
                    imageVector = Icons.Outlined.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp),
                )
                Text(
                    text = stringResource(R.string.add_meal_to_statistics),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedTextField(
                        value = timeofEnter.value,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.time_of_enter)) },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showEnterDialog.value = true },
                    )
                    OutlinedTextField(
                        value = timeofExit.value,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.time_of_exit)) },
                        enabled = false,
                        readOnly = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showExitDialog.value = true },
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedMeal.asString(context),
                        onValueChange = {},
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        mealNames.forEach { meal ->
                            DropdownMenuItem(
                                onClick = {
                                    isExpanded = false
                                    selectedMeal = Uitext.StringResource(mealNameToRes(meal))
                                },
                                text = { Text(Uitext.StringResource(mealNameToRes(meal)).asString(context)) },
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.discard))
                    }
                    Button(
                        onClick = {
                            try {
                                val statisticsMeal = EatingStatisticsData(
                                    day.value,
                                    timeofEnter.value,
                                    timeofExit.value,
                                    selectedMeal,
                                )
                                scope.launch {
                                    viewModel.addMealEvent(statisticsMeal, monthName)
                                }
                            } catch (_: Exception) {
                            }
                            onDismissRequest()
                        },
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofMealPicker(
    dateofMeal: MutableState<String>,
    showMealDialog: MutableState<Boolean>,
    mealDialogState: DatePickerState
) {
    DatePickerDialog(
        onDismissRequest = { showMealDialog.value = false },
        confirmButton = {
            TextButton(
                onClick = {
                    showMealDialog.value = false
                    dateofMeal.value =
                        mealDialogState.selectedDateMillis?.convertMillisToDate() ?: ""
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = { showMealDialog.value = false }) {
                Text(stringResource(R.string.discard))
            }
        },
    ) {
        DatePicker(
            state = mealDialogState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                selectedYearContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                selectedYearContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                todayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    timeState: TimePickerState,
    enteredtime: MutableState<String>,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.discard))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                enteredtime.value = "%02d:%02d".format(timeState.hour, timeState.minute)
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { content() },
    )
}
