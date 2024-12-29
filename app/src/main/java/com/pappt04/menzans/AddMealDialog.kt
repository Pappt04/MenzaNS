package com.pappt04.menzans

import android.content.Context
import androidx.collection.LongList
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pappt04.menzans.DummyData.MealSample
import com.pappt04.menzans.DummyData.engmeals
import com.pappt04.menzans.DummyData.engtosresc
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(onDismissRequest: () -> Unit, context: Context, day: MutableState<LocalDate>) {
    /*
    SAVED FOR FUTURE USE
    var dateofMeal = remember { mutableStateOf("") }
    var showMealDialog = remember { mutableStateOf(false) }
    var mealDialogState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    */

    val timeofEnter = remember { mutableStateOf("") }
    val showEnterDialog = remember { mutableStateOf(false) }
    val enterPickerState = rememberTimePickerState(0, 0, true)

    val timeofExit = remember { mutableStateOf("") }
    val showExitDialog = remember { mutableStateOf(false) }
    val exitPickerState = rememberTimePickerState(0, 0, true)

    var selectedMeal by remember { mutableStateOf(Uitext.StringResource(R.string.breakfast)) }

    var isExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add meal to statistics",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Row()
                {

                    OutlinedTextField(
                        value = timeofEnter.value,
                        onValueChange = { print("Clicked") },
                        label = { Text(text = "Time of enter") },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { showEnterDialog.value = !showEnterDialog.value }
                            .weight(1f),
                    )
                    if (showEnterDialog.value) {
                        TimePickerDialog(
                            onDismiss = { showEnterDialog.value = false },
                            timeState = enterPickerState,
                            enteredtime = timeofEnter
                        ) {
                            TimePicker(
                                state = enterPickerState,
                            )
                        }
                    }
                    OutlinedTextField(
                        value = timeofExit.value,
                        onValueChange = { print("Clicked") },
                        label = { Text(text = "Time of exit") },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { showExitDialog.value = !showExitDialog.value }
                            .weight(1f),
                    )
                    if (showExitDialog.value) {
                        TimePickerDialog(
                            onDismiss = { showExitDialog.value = false },
                            timeState = exitPickerState,
                            enteredtime = timeofExit
                        ) {
                            TimePicker(
                                state = exitPickerState,
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = !isExpanded },
                    modifier = Modifier
                ) {
                    TextField(
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = selectedMeal.asString(context),
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {
                        engmeals.forEach { meal ->
                            DropdownMenuItem(
                                onClick = {
                                    isExpanded = false
                                    selectedMeal = Uitext.StringResource(engtosresc(meal))
                                },
                                text = { Text(Uitext.StringResource(engtosresc(meal)).asString(context)) }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                    ) {
                        Text(stringResource(R.string.discard))
                    }
                    Button(onClick = {
                        try {
                            val eat= EatingStatisticsData(day.value,timeofEnter.value,timeofExit.value,selectedMeal)
                            var sdao= StatisticsFileDAO(context,DummyData.engmonths[day.value.monthValue-1])
                            sdao.appendToStatisticsFile(eat)
                        } catch (_: Exception) {
                        }
                        onDismissRequest()
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

fun getUniversalLanguageMeal(context: Context, meal: String): Uitext {
    var i = 0
    for (m in engmeals) {
        if (meal == m)
            return MealSample[i].name
        i++
    }
    return MealSample[0].name
}

fun getEngLanguageMeal(context: Context,meal: String): String
{
    return ""
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofMealPicker(
    dateofMeal: MutableState<String>,
    showMealDialog: MutableState<Boolean>,
    mealDialogState: DatePickerState
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        onDismissRequest = {
            // Action when the dialog is dismissed without selecting a date
            showMealDialog.value = false
        },
        confirmButton = {
            // Confirm button with custom action and styling
            TextButton(
                onClick = {
                    // Action to set the selected date and close the dialog
                    showMealDialog.value = false
                    dateofMeal.value =
                        mealDialogState.selectedDateMillis?.convertMillisToDate() ?: ""
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showMealDialog.value = false
                }
            ) {
                Text(stringResource(R.string.discard))
            }
        }
    ) {
        // The actual DatePicker component within the dialog
        DatePicker(
            state = mealDialogState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedDayContentColor = MaterialTheme.colorScheme.primary,
                selectedYearContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                selectedYearContentColor = MaterialTheme.colorScheme.secondary,
                todayContentColor = MaterialTheme.colorScheme.tertiary,
                todayDateBorderColor = MaterialTheme.colorScheme.tertiaryContainer
            )
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
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.discard))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                enteredtime.value = "${timeState.hour}:${timeState.minute}"
                onDismiss()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        text = { content() }
    )
}