package com.pappt04.menzans.views.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.pappt04.menzans.R
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.ui.theme.IconSize
import com.pappt04.menzans.ui.theme.Spacing
import com.pappt04.menzans.viewmodels.CardViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(
    snackbar: SnackbarHostState,
    maindrawerpadding: PaddingValues,
    viewModel: CardViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cardInfo by viewModel.cardInfo.collectAsState()
    val mealCounts by viewModel.mealCounts.collectAsState()
    val tokenWarningLimit by viewModel.tokenWarningLimit.collectAsState()

    var surname by remember { mutableStateOf(cardInfo.surname) }
    var name by remember { mutableStateOf(cardInfo.name) }
    var index by remember { mutableStateOf(cardInfo.index) }
    var cardnumber by remember { mutableStateOf(cardInfo.cardnumber) }
    var isicCardNumber by remember { mutableStateOf(cardInfo.isicnumber) }
    var faculty by remember { mutableStateOf(cardInfo.faculty) }

    val dateofBirth = remember { mutableStateOf(cardInfo.dateofbirth) }
    val birthDialogState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showBirthDialog = remember { mutableStateOf(false) }

    val cardIssued = remember { mutableStateOf(cardInfo.issued) }
    val issuedState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showIssuedDialog = remember { mutableStateOf(false) }

    val cardValid = remember { mutableStateOf(cardInfo.validuntil) }
    val validState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showValidDialog = remember { mutableStateOf(false) }

    // Sync local form state whenever the ViewModel emits (initial load or after discard)
    LaunchedEffect(cardInfo) {
        surname = cardInfo.surname
        name = cardInfo.name
        index = cardInfo.index
        cardnumber = cardInfo.cardnumber
        isicCardNumber = cardInfo.isicnumber
        faculty = cardInfo.faculty
        dateofBirth.value = cardInfo.dateofbirth
        cardIssued.value = cardInfo.issued
        cardValid.value = cardInfo.validuntil
    }

    LazyColumn(
        modifier = Modifier.padding(maindrawerpadding),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            StudentCardPreview(
                name = name,
                surname = surname,
                faculty = faculty,
                index = index,
                validUntil = cardValid.value,
                onShare = {
                    val intent =
                        viewModel.shareCard(
                            CardPreferences(
                                surname = surname,
                                name = name,
                                index = index,
                                cardnumber = cardnumber,
                                isicnumber = isicCardNumber,
                                faculty = faculty,
                                dateofbirth = dateofBirth.value,
                                issued = cardIssued.value,
                                validuntil = cardValid.value,
                            ),
                        )
                    startActivity(context, intent, null)
                },
            )
        }

        // Token summary
        item {
            TokenSummaryRow(
                mealCounts = mealCounts,
                warningLimit = tokenWarningLimit,
            )
        }

        // Personal info section
        item {
            SectionHeader(
                icon = Icons.Default.School,
                title = stringResource(R.string.studies_at),
            )
        }
        item {
            Column(modifier = Modifier.padding(horizontal = Spacing.md)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text(stringResource(R.string.surname)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = faculty,
                    onValueChange = { faculty = it },
                    label = { Text(stringResource(R.string.studies_at)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Card numbers section
        item {
            SectionHeader(
                icon = Icons.Default.CreditCard,
                title = stringResource(R.string.card_number),
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = cardnumber,
                    onValueChange = { cardnumber = it },
                    label = { Text(stringResource(R.string.card_number)) },
                    leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = isicCardNumber,
                    onValueChange = { isicCardNumber = it },
                    label = { Text(stringResource(R.string.isic_card_number)) },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = index,
                    onValueChange = { index = it },
                    label = { Text(stringResource(R.string.index)) },
                    leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Dates section
        item {
            SectionHeader(
                icon = Icons.Default.CalendarMonth,
                title = stringResource(R.string.date_of_birth),
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = Spacing.md),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DateFieldRow(
                    label = stringResource(R.string.date_of_birth),
                    value = dateofBirth.value,
                    onClick = { showBirthDialog.value = true },
                )
                DateFieldRow(
                    label = stringResource(R.string.issued_date),
                    value = cardIssued.value,
                    onClick = { showIssuedDialog.value = true },
                )
                DateFieldRow(
                    label = stringResource(R.string.valid_until),
                    value = cardValid.value,
                    onClick = { showValidDialog.value = true },
                )
            }
        }

        // Action buttons
        item {
            Spacer(Modifier.height(Spacing.md))
            ActionButtons(
                onDiscard = {
                    surname = cardInfo.surname
                    name = cardInfo.name
                    index = cardInfo.index
                    cardnumber = cardInfo.cardnumber
                    isicCardNumber = cardInfo.isicnumber
                    faculty = cardInfo.faculty
                    dateofBirth.value = cardInfo.dateofbirth
                    cardIssued.value = cardInfo.issued
                    cardValid.value = cardInfo.validuntil
                },
                onSave = {
                    viewModel.saveCardInfo(
                        CardPreferences(
                            surname = surname,
                            name = name,
                            faculty = faculty,
                            dateofbirth = dateofBirth.value,
                            issued = cardIssued.value,
                            validuntil = cardValid.value,
                            index = index,
                            cardnumber = cardnumber,
                            isicnumber = isicCardNumber,
                        ),
                    )
                    scope.launch {
                        snackbar.showSnackbar("Sačuvano", duration = SnackbarDuration.Short)
                    }
                },
            )
        }
    }

    if (showBirthDialog.value) {
        DateofBirthPicker(dateofBirth, showBirthDialog, birthDialogState)
    }
    if (showIssuedDialog.value) {
        IssuedPicker(cardIssued, showIssuedDialog, issuedState)
    }
    if (showValidDialog.value) {
        ValidPicker(cardValid, showValidDialog, validState)
    }
}

// ────────────────────────────────────────────────────
// Visual card preview
// ────────────────────────────────────────────────────

@Composable
private fun StudentCardPreview(
    name: String,
    surname: String,
    faculty: String,
    index: String,
    validUntil: String,
    onShare: () -> Unit,
) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    ElevatedCard(
        shape = RoundedCornerShape(Spacing.lg),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm)
                .height(180.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(primary, primaryContainer),
                        ),
                    ).padding(Spacing.md),
        ) {
            // Logos top-right
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isic_logo),
                    contentDescription = stringResource(R.string.international_student_identity_card_logo_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(52.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.eyca_logo),
                    contentDescription = stringResource(R.string.logo_off_european_youth_card_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(36.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.coat_of_arms_of_serbia_small),
                    contentDescription = stringResource(R.string.coat_of_arms_of_serbia_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(22.dp),
                )
            }

            // Name + faculty
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text =
                        if (surname.isBlank() && name.isBlank()) {
                            "Ime Prezime"
                        } else {
                            "$surname $name".trim()
                        },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (faculty.isNotBlank()) {
                    Text(
                        text = faculty,
                        style = MaterialTheme.typography.bodySmall,
                        color = onPrimary.copy(alpha = 0.75f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (index.isNotBlank()) {
                    Text(
                        text = index,
                        style = MaterialTheme.typography.bodySmall,
                        color = onPrimary.copy(alpha = 0.75f),
                    )
                }
            }

            // Bottom-start: validity text
            if (validUntil.isNotBlank()) {
                Text(
                    text = "Važi do: $validUntil",
                    style = MaterialTheme.typography.labelSmall,
                    color = onPrimary.copy(alpha = 0.75f),
                    modifier = Modifier.align(Alignment.BottomStart),
                )
            }

            // Bottom-end: share button
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.share),
                tint = onPrimary.copy(alpha = 0.75f),
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .clickable { onShare() }
                        .size(IconSize.medium),
            )
        }
    }
}

// ────────────────────────────────────────────────────
// Token summary (read-only)
// ────────────────────────────────────────────────────

@Composable
private fun TokenSummaryRow(
    mealCounts: com.pappt04.menzans.models.MealPreferences,
    warningLimit: Int,
) {
    val meals =
        listOf(
            Triple(stringResource(R.string.breakfast), mealCounts.breakfast, Icons.Default.Coffee),
            Triple(stringResource(R.string.lunch), mealCounts.lunch, Icons.Filled.Restaurant),
            Triple(stringResource(R.string.dinner), mealCounts.dinner, Icons.Outlined.Fastfood),
        )

    LazyRow(
        contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.sm),
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        items(meals.size) { i ->
            val (label, count, icon) = meals[i]
            TokenChip(label, count, warningLimit, icon)
        }
    }
}

@Composable
private fun TokenChip(
    label: String,
    count: Int,
    warningLimit: Int,
    icon: ImageVector,
) {
    val low = count <= warningLimit
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (low) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer
                    },
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(IconSize.small),
                tint =
                    if (low) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color =
                    if (low) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    },
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color =
                    if (low) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.secondary
                    },
            )
        }
    }
}

// ────────────────────────────────────────────────────
// Section header
// ────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    icon: ImageVector,
    title: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = Spacing.md, end = Spacing.md, top = Spacing.md, bottom = Spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(IconSize.medium),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

// ────────────────────────────────────────────────────
// Date field row
// ────────────────────────────────────────────────────

@Composable
private fun DateFieldRow(
    label: String,
    value: String,
    onClick: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        leadingIcon = {
            Icon(Icons.Default.CalendarMonth, contentDescription = null)
        },
        enabled = false,
        readOnly = true,
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
    )
}

// ────────────────────────────────────────────────────
// Action buttons
// ────────────────────────────────────────────────────

@Composable
private fun ActionButtons(
    onDiscard: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
    ) {
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.save))
        }
        FilledTonalButton(
            onClick = onDiscard,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.discard))
        }
    }
}

// ────────────────────────────────────────────────────
// Date picker dialogs (unchanged logic, shared styling)
// ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateofBirthPicker(
    dateofBirth: MutableState<String>,
    showBirthDialog: MutableState<Boolean>,
    birthDialogState: DatePickerState,
) {
    MenzaDatePickerDialog(
        onDismiss = { showBirthDialog.value = false },
        onConfirm = {
            dateofBirth.value = birthDialogState.selectedDateMillis?.convertMillisToDate() ?: ""
            showBirthDialog.value = false
        },
    ) {
        DatePicker(state = birthDialogState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuedPicker(
    cardIssued: MutableState<String>,
    showIssuedDialog: MutableState<Boolean>,
    issuedState: DatePickerState,
) {
    MenzaDatePickerDialog(
        onDismiss = { showIssuedDialog.value = false },
        onConfirm = {
            cardIssued.value = issuedState.selectedDateMillis?.convertMillisToDate() ?: ""
            showIssuedDialog.value = false
        },
    ) {
        DatePicker(state = issuedState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidPicker(
    cardValid: MutableState<String>,
    showValidDialog: MutableState<Boolean>,
    validState: DatePickerState,
) {
    MenzaDatePickerDialog(
        onDismiss = { showValidDialog.value = false },
        onConfirm = {
            cardValid.value = validState.selectedDateMillis?.convertMillisToDate() ?: ""
            showValidDialog.value = false
        },
    ) {
        DatePicker(state = validState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenzaDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    DatePickerDialog(
        colors =
            DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.discard)) }
        },
    ) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun menzaDatePickerColors() =
    DatePickerDefaults.colors(
        selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedDayContentColor = MaterialTheme.colorScheme.primary,
        selectedYearContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        selectedYearContentColor = MaterialTheme.colorScheme.secondary,
        todayContentColor = MaterialTheme.colorScheme.tertiary,
        todayDateBorderColor = MaterialTheme.colorScheme.tertiaryContainer,
    )

// ────────────────────────────────────────────────────
// Utilities
// ────────────────────────────────────────────────────

fun Long.convertMillisToDate(): String {
    val calendar =
        Calendar.getInstance().apply {
            timeInMillis = this@convertMillisToDate
            val zoneOffset = get(Calendar.ZONE_OFFSET)
            val dstOffset = get(Calendar.DST_OFFSET)
            add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
        }
    val sdf = SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH)
    return sdf.format(calendar.time)
}
