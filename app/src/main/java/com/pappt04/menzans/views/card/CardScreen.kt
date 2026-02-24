package com.pappt04.menzans.views.card


import android.content.Intent
import android.content.res.Configuration
import com.pappt04.menzans.views.MainActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.pappt04.menzans.R
import com.pappt04.menzans.data.local.datastore.CardDataStoreManager
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import com.pappt04.menzans.models.MealPreferences
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    remainingOnCard: SnapshotStateList<Int>,
    snackbar: SnackbarHostState,
    maindrawerpadding: PaddingValues
) {

    val context = LocalContext.current

    var cardprefs = remember { CardPreferences() }

    val scope = rememberCoroutineScope()

    var surname by remember { mutableStateOf(cardprefs.surname) }
    var name by remember { mutableStateOf(cardprefs.name) }
    var index by remember { mutableStateOf(cardprefs.index) }
    var cardnumber by remember { mutableStateOf(cardprefs.cardnumber) }

    var ISICcardnumber by remember { mutableStateOf(cardprefs.isicnumber) }
    var universityandfaculty by remember { mutableStateOf(cardprefs.faculty) }

    var dateofBirth = remember { mutableStateOf(cardprefs.dateofbirth) }
    val birthDialogState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showBirthDialog = remember { mutableStateOf(false) }

    val cardIssued = remember { mutableStateOf(cardprefs.issued) }
    val IssuedState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showIssuedDialog = remember { mutableStateOf(false) }

    val cardValid = remember { mutableStateOf(cardprefs.validuntil) }
    val ValidState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showValidDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val prfs = CardDataStoreManager(context)
        cardprefs = prfs.getFromDataStore().first()

        surname = cardprefs.surname
        name = cardprefs.name
        index = cardprefs.index
        cardnumber = cardprefs.cardnumber
        ISICcardnumber = cardprefs.isicnumber
        universityandfaculty = cardprefs.faculty

        dateofBirth.value = cardprefs.dateofbirth
        cardIssued.value = cardprefs.issued
        cardValid.value = cardprefs.validuntil
    }


    val editbreakfast = remember { mutableIntStateOf(remainingOnCard[0]) }
    val editlunch = remember { mutableIntStateOf(remainingOnCard[1]) }
    val editdinner = remember { mutableIntStateOf(remainingOnCard[2]) }
    val editbalance = remember { mutableIntStateOf(remainingOnCard[3]) }

    LazyColumn(
        modifier = Modifier
            .padding(maindrawerpadding)
    ) {
        item {
            OutlinedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .padding(4.dp)
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
                            contentDescription = stringResource(R.string.international_student_identity_card_logo_description),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.eyca_logo),
                            contentDescription = stringResource(R.string.logo_off_european_youth_card_description),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.coat_of_arms_of_serbia_small),
                            contentDescription = stringResource(R.string.coat_of_arms_of_serbia_description),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(4.dp)
                                .weight(1f)
                        )
                    }
                    Row {
                        OutlinedTextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = { Text(stringResource(R.string.surname)) },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text(stringResource(R.string.name)) },
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = universityandfaculty,
                        onValueChange = { universityandfaculty = it },
                        label = { Text(text = stringResource(R.string.studies_at)) },
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    )
                    Row {
                        OutlinedTextField(
                            value = dateofBirth.value,
                            onValueChange = { print("Clicked") },
                            label = { Text(text = stringResource(R.string.date_of_birth)) },
                            enabled = false,
                            readOnly = true,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { showBirthDialog.value = !showBirthDialog.value }
                                .weight(1f),
                        )
                        if (showBirthDialog.value)
                            DateofBirthPicker(dateofBirth, showBirthDialog, birthDialogState)

                        OutlinedTextField(
                            value = cardIssued.value,
                            onValueChange = { print("Clicked") },
                            label = { Text(text = stringResource(R.string.issued_date)) },
                            enabled = false,
                            readOnly = true,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { showIssuedDialog.value = !showIssuedDialog.value }
                                .weight(1f),
                        )
                        if (showIssuedDialog.value)
                            IssuedPicker(cardIssued, showIssuedDialog, IssuedState)

                        OutlinedTextField(
                            value = cardValid.value,
                            onValueChange = { print("Clicked") },
                            label = { Text(text = stringResource(R.string.valid_until)) },
                            enabled = false,
                            readOnly = true,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable { showValidDialog.value = !showValidDialog.value }
                                .weight(1f),
                        )
                        if (showValidDialog.value)
                            ValidPicker(cardValid, showValidDialog, ValidState)
                    }
                    OutlinedTextField(
                        value = index,
                        onValueChange = { index = it },
                        label = { Text(text = stringResource(R.string.index)) },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    OutlinedTextField(
                        value = cardnumber,
                        onValueChange = { cardnumber = it },
                        label = { Text(text = stringResource(R.string.card_number)) },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    OutlinedTextField(
                        value = ISICcardnumber,
                        onValueChange = { ISICcardnumber = it },
                        label = { Text(text = stringResource(R.string.isic_card_number)) },
                        modifier = Modifier
                            .padding(4.dp)
                    )
                    IconButton(onClick =
                        {
                            val str= "Prezime: $surname\n" +
                                     "Ime: $name\n" +
                                     "Univerzitet: Univerzitet u Novom Sadu" +
                                     "Fakultet: $universityandfaculty\n" +
                                     "Indeks: $index\n" +
                                     "Datum rođenja: $dateofBirth\n" +
                                     "Datum Izdavanja: $cardIssued\n" +
                                     "Važi do: $cardValid\n" +
                                     "Broj kartice: $cardnumber\n" +
                                     "ISIC broj kartice: $ISICcardnumber\n"

                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, str)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)

                            startActivity(context, shareIntent, null)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }
        }
        item {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
//        item {
//            OutlinedCard(
//                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                ),
//                border = BorderStroke(1.dp, Color.Black),
//                modifier = Modifier
//                    .padding(8.dp)
//            ) {
//                Column {
//                    Row {
//                        OutlinedTextField(
//                            value = editbreakfast.intValue.toString(),
//                            onValueChange = {
//                                try {
//                                    editbreakfast.intValue = it.toInt()
//                                } catch (e: Exception) {
//                                    editbreakfast.intValue = 0
//                                }
//                            },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            label = { Text(stringResource(R.string.breakfast) + ": ${remainingOnCard[0]}") },
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .fillMaxWidth()
//                                .weight(1f)
//                        )
//                        OutlinedTextField(
//                            value = editlunch.intValue.toString(),
//                            onValueChange = {
//                                try {
//                                    editlunch.intValue = it.toInt()
//                                } catch (e: Exception) {
//                                    editlunch.intValue = 0
//                                }
//                            },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            label = { Text(stringResource(R.string.lunch) + ": ${remainingOnCard[1]}") },
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .fillMaxWidth()
//                                .weight(1f)
//                        )
//                        OutlinedTextField(
//                            value = editdinner.intValue.toString(),
//                            onValueChange = {
//                                try {
//                                    editdinner.intValue = it.toInt()
//                                } catch (e: Exception) {
//                                    editdinner.intValue = 0
//                                }
//                            },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            label = { Text(stringResource(R.string.dinner) + ": ${remainingOnCard[2]}") },
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .fillMaxWidth()
//                                .weight(1f)
//                        )
//                    }
//                    OutlinedTextField(
//                        value = editbalance.intValue.toString(),
//                        onValueChange = {
//                            try {
//                                editbalance.intValue = it.toInt()
//                            } catch (e: Exception) {
//                                editbalance.intValue=0
//                            }
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        label = { Text(stringResource(R.string.balance) + ": ${remainingOnCard[3]}") },
//                        textStyle = LocalTextStyle.current.copy(
//                            textAlign = TextAlign.Right,
//                            fontSize = 22.sp
//                        ),
//                        prefix = {
//                            Text(
//                                text = "+",
//                                fontSize = 22.sp
//                            )
//                        },
//                        suffix = {
//                            Text(
//                                stringResource(R.string.rsd),
//                                fontSize = 22.sp
//                            )
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    )
//                }
//            }
//        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(10.dp)
                ) {
                    Text(stringResource(R.string.discard))
                }
                Button(
                    onClick = {
                        scope.launch {
                            val cman = CardDataStoreManager(context)
                            cman.saveToDataStore(
                                CardPreferences(
                                    surname = surname,
                                    name = name,
                                    faculty = universityandfaculty,
                                    dateofbirth = dateofBirth.value,
                                    issued = cardIssued.value,
                                    validuntil = cardValid.value,
                                    index = index,
                                    cardnumber = cardnumber,
                                    isicnumber = ISICcardnumber,
                                )
                            )

                            val mman = MealDataStoreManager(context)
                            mman.saveToDataStore(
                                MealPreferences(
                                    breakfast = editbreakfast.intValue,
                                    lunch = editlunch.intValue,
                                    dinner = editdinner.intValue,
                                    balance = editbalance.intValue
                                )
                            )
                            snackbar.showSnackbar(
                                message = "Saved",
                                duration = SnackbarDuration.Short
                            )

                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(10.dp)
                ) {
                    Text(stringResource(R.string.save))
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showBirthDialog.value = false
                }
            ) {
                Text(stringResource(R.string.discard))
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showValidDialog.value = false
                }
            ) {
                Text(stringResource(R.string.discard))
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            // Dismiss button to close the dialog without selecting a date
            TextButton(
                onClick = {
                    showIssuedDialog.value = false
                }
            ) {
                Text(stringResource(R.string.discard))
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

fun indexToLetter(index: Int): String {
    return ('A' + index).toString()
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewEditScreen() {
    val snack = remember { SnackbarHostState() }

    MenzaNSTheme {
        val remainingOnCard = remember { mutableStateListOf(10, 20, 30, 40, 50, 60, 70, 80, 90) }
        MaterialTheme {
            CardScreen(remainingOnCard, snack, PaddingValues(0.dp))
        }
    }
}
