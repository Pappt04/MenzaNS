package com.pappt04.menzans


import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen() {
    var surname by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var index by remember { mutableStateOf("") }
    var cardnumber by remember { mutableStateOf("") }

    var ISICcardnumber by remember { mutableStateOf("") }
    var universityandfaculty by remember { mutableStateOf("") }

    val dateofBirth = remember { mutableStateOf("") }
    val birthDialogState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showBirthDialog = remember { mutableStateOf(false) }

    val cardIssued = remember { mutableStateOf("") }
    val IssuedState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showIssuedDialog = remember { mutableStateOf(false) }

    val cardValid = remember { mutableStateOf("") }
    val ValidState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showValidDialog = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "",
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .padding(10.dp)
                    ) {
                        Text("Discard")
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .padding(10.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        })
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color.Black)
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.isic_logo),
                            contentDescription = "A call icon for calling",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.eyca_logo),
                            contentDescription = "A call icon for calling",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.coat_of_arms_of_serbia_small),
                            contentDescription = "A call icon for calling",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                    }
                    Row() {
                        TextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = { Text("Surname:") },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name:") },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                    TextField(
                        value= universityandfaculty,
                        onValueChange = {universityandfaculty = it},
                        label = { Text(text = "Studies at:") },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                    TextField(
                        value = dateofBirth.value,
                        onValueChange = { print("Clicked") },
                        label = { Text(text = "Date of Birth:") },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { showBirthDialog.value = !showBirthDialog.value },
                    )
                    if (showBirthDialog.value)
                        DateofBirthPicker(dateofBirth, showBirthDialog, birthDialogState)

                    TextField(
                        value = cardIssued.value,
                        onValueChange = { print("Clicked") },
                        label = { Text(text = "Issued Date:") },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { showIssuedDialog.value = !showIssuedDialog.value },
                    )
                    if (showIssuedDialog.value)
                        IssuedPicker(cardIssued, showIssuedDialog, IssuedState)

                    TextField(
                        value = cardValid.value,
                        onValueChange = { print("Clicked") },
                        label = { Text(text = "Valid until:") },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { showValidDialog.value = !showValidDialog.value },
                    )
                    if (showValidDialog.value)
                        ValidPicker(cardValid, showValidDialog, ValidState)

                    TextField(
                        value = index,
                        onValueChange = { index = it },
                        label = { Text(text = "Index:") },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    TextField(
                        value = cardnumber,
                        onValueChange = { cardnumber = it },
                        label = { Text(text = "Card number:") },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    TextField(
                        value = ISICcardnumber,
                        onValueChange = { ISICcardnumber = it },
                        label = { Text(text = "ISIC card number:") },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofBirthPicker(
    dateofBirth: MutableState<String>,
    showBirthDialog: MutableState<Boolean>,
    birthDialogState: DatePickerState
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFFF5F0FF),
        ),
        onDismissRequest = {
            // Action when the dialog is dismissed without selecting a date
            showBirthDialog.value = false
        },
        confirmButton = {
            // Confirm button with custom action and styling
            TextButton(
                onClick = {
                    // Action to set the selected date and close the dialog
                    showBirthDialog.value = false
                    dateofBirth.value =
                        birthDialogState.selectedDateMillis?.convertMillisToDate() ?: ""
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showBirthDialog.value = false
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        // The actual DatePicker component within the dialog
        DatePicker(
            state = birthDialogState,
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
fun ValidPicker(
    cardValid: MutableState<String>,
    showValidDialog: MutableState<Boolean>,
    validState: DatePickerState
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFFF5F0FF),
        ),
        onDismissRequest = {
            // Action when the dialog is dismissed without selecting a date
            showValidDialog.value = false
        },
        confirmButton = {
            // Confirm button with custom action and styling
            TextButton(
                onClick = {
                    // Action to set the selected date and close the dialog
                    showValidDialog.value = false
                    cardValid.value =
                        validState.selectedDateMillis?.convertMillisToDate() ?: ""
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showValidDialog.value = false
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        // The actual DatePicker component within the dialog
        DatePicker(
            state = validState,
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
fun IssuedPicker(
    cardIssued: MutableState<String>,
    showIssuedDialog: MutableState<Boolean>,
    issuedState: DatePickerState
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = Color(0xFFF5F0FF),
        ),
        onDismissRequest = {
            // Action when the dialog is dismissed without selecting a date
            showIssuedDialog.value = false
        },
        confirmButton = {
            // Confirm button with custom action and styling
            TextButton(
                onClick = {
                    // Action to set the selected date and close the dialog
                    showIssuedDialog.value = false
                    cardIssued.value =
                        issuedState.selectedDateMillis?.convertMillisToDate() ?: ""
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showIssuedDialog.value = false
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        // The actual DatePicker component within the dialog
        DatePicker(
            state = issuedState,
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

fun Long.convertMillisToDate(): String {
    // Create a calendar instance in the default time zone
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
        // Adjust for the time zone offset to get the correct local date
        val zoneOffset = get(Calendar.ZONE_OFFSET)
        val dstOffset = get(Calendar.DST_OFFSET)
        add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    }
    val sdf = SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH)
    return sdf.format(calendar.time)
}




@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewEditScreen() {
    MenzaNSTheme {
        EditScreen()
    }
}
