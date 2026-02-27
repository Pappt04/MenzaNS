package com.pappt04.menzans.views.card

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.pappt04.menzans.R
import com.pappt04.menzans.data.local.datastore.CardDataStoreManager
import com.pappt04.menzans.models.CardPreferences
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
    var isicCardNumber by remember { mutableStateOf(cardprefs.isicnumber) }
    var universityandfaculty by remember { mutableStateOf(cardprefs.faculty) }

    val dateofBirth = remember { mutableStateOf(cardprefs.dateofbirth) }
    val birthDialogState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showBirthDialog = remember { mutableStateOf(false) }

    val cardIssued = remember { mutableStateOf(cardprefs.issued) }
    val issuedState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showIssuedDialog = remember { mutableStateOf(false) }

    val cardValid = remember { mutableStateOf(cardprefs.validuntil) }
    val validState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
    val showValidDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val prfs = CardDataStoreManager(context)
        cardprefs = prfs.getFromDataStore().first()
        surname = cardprefs.surname
        name = cardprefs.name
        index = cardprefs.index
        cardnumber = cardprefs.cardnumber
        isicCardNumber = cardprefs.isicnumber
        universityandfaculty = cardprefs.faculty
        dateofBirth.value = cardprefs.dateofbirth
        cardIssued.value = cardprefs.issued
        cardValid.value = cardprefs.validuntil
    }

    LazyColumn(
        modifier = Modifier.padding(maindrawerpadding),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Visual card preview
        item {
            StudentCardPreview(
                name = name,
                surname = surname,
                faculty = universityandfaculty,
                index = index,
                validUntil = cardValid.value
            )
        }

        // Token summary
        item {
            TokenSummaryRow(remainingOnCard)
        }

        // Personal info section
        item {
            SectionHeader(
                icon = Icons.Default.School,
                title = stringResource(R.string.studies_at)
            )
        }
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = surname,
                        onValueChange = { surname = it },
                        label = { Text(stringResource(R.string.surname)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(stringResource(R.string.name)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = universityandfaculty,
                    onValueChange = { universityandfaculty = it },
                    label = { Text(stringResource(R.string.studies_at)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Card numbers section
        item {
            SectionHeader(
                icon = Icons.Default.CreditCard,
                title = stringResource(R.string.card_number)
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = cardnumber,
                    onValueChange = { cardnumber = it },
                    label = { Text(stringResource(R.string.card_number)) },
                    leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = isicCardNumber,
                    onValueChange = { isicCardNumber = it },
                    label = { Text(stringResource(R.string.isic_card_number)) },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = index,
                    onValueChange = { index = it },
                    label = { Text(stringResource(R.string.index)) },
                    leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Dates section
        item {
            SectionHeader(
                icon = Icons.Default.CalendarMonth,
                title = stringResource(R.string.date_of_birth)
            )
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateFieldRow(
                    label = stringResource(R.string.date_of_birth),
                    value = dateofBirth.value,
                    onClick = { showBirthDialog.value = true }
                )
                DateFieldRow(
                    label = stringResource(R.string.issued_date),
                    value = cardIssued.value,
                    onClick = { showIssuedDialog.value = true }
                )
                DateFieldRow(
                    label = stringResource(R.string.valid_until),
                    value = cardValid.value,
                    onClick = { showValidDialog.value = true },
                    validityStatus = cardValid.value.validityStatus()
                )
            }
        }

        // Action buttons
        item {
            Spacer(Modifier.height(16.dp))
            ActionButtons(
                onShare = {
                    val str = buildString {
                        appendLine("Prezime: $surname")
                        appendLine("Ime: $name")
                        appendLine("Univerzitet: Univerzitet u Novom Sadu")
                        appendLine("Fakultet: $universityandfaculty")
                        appendLine("Indeks: $index")
                        appendLine("Datum rođenja: ${dateofBirth.value}")
                        appendLine("Datum Izdavanja: ${cardIssued.value}")
                        appendLine("Važi do: ${cardValid.value}")
                        appendLine("Broj kartice: $cardnumber")
                        appendLine("ISIC broj kartice: $isicCardNumber")
                    }
                    val shareIntent = Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, str)
                            type = "text/plain"
                        }, null
                    )
                    startActivity(context, shareIntent, null)
                },
                onDiscard = {
                    scope.launch {
                        val saved = CardDataStoreManager(context).getFromDataStore().first()
                        surname = saved.surname
                        name = saved.name
                        index = saved.index
                        cardnumber = saved.cardnumber
                        isicCardNumber = saved.isicnumber
                        universityandfaculty = saved.faculty
                        dateofBirth.value = saved.dateofbirth
                        cardIssued.value = saved.issued
                        cardValid.value = saved.validuntil
                    }
                },
                onSave = {
                    scope.launch {
                        CardDataStoreManager(context).saveToDataStore(
                            CardPreferences(
                                surname = surname,
                                name = name,
                                faculty = universityandfaculty,
                                dateofbirth = dateofBirth.value,
                                issued = cardIssued.value,
                                validuntil = cardValid.value,
                                index = index,
                                cardnumber = cardnumber,
                                isicnumber = isicCardNumber,
                            )
                        )
                        snackbar.showSnackbar(
                            message = "Sačuvano",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        }
    }

    if (showBirthDialog.value)
        DateofBirthPicker(dateofBirth, showBirthDialog, birthDialogState)
    if (showIssuedDialog.value)
        IssuedPicker(cardIssued, showIssuedDialog, issuedState)
    if (showValidDialog.value)
        ValidPicker(cardValid, showValidDialog, validState)
}

// ────────────────────────────────────────────────────
// Visual card preview
// ────────────────────────────────────────────────────

private enum class ValidityStatus { VALID, EXPIRING_SOON, EXPIRED, UNKNOWN }

private fun String.validityStatus(): ValidityStatus {
    if (isBlank()) return ValidityStatus.UNKNOWN
    return try {
        val sdf = SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH)
        val date = sdf.parse(this) ?: return ValidityStatus.UNKNOWN
        val now = Date()
        val daysLeft = (date.time - now.time) / (1000 * 60 * 60 * 24)
        when {
            daysLeft < 0 -> ValidityStatus.EXPIRED
            daysLeft < 30 -> ValidityStatus.EXPIRING_SOON
            else -> ValidityStatus.VALID
        }
    } catch (_: Exception) {
        ValidityStatus.UNKNOWN
    }
}

@Composable
private fun StudentCardPreview(
    name: String,
    surname: String,
    faculty: String,
    index: String,
    validUntil: String
) {
    val primary = MaterialTheme.colorScheme.primary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    val status = validUntil.validityStatus()
    val statusColor = when (status) {
        ValidityStatus.VALID -> Color(0xFF4CAF50)
        ValidityStatus.EXPIRING_SOON -> Color(0xFFFFC107)
        ValidityStatus.EXPIRED -> MaterialTheme.colorScheme.error
        ValidityStatus.UNKNOWN -> MaterialTheme.colorScheme.outline
    }
    val statusLabel = when (status) {
        ValidityStatus.VALID -> "Aktivna"
        ValidityStatus.EXPIRING_SOON -> "Uskoro ističe"
        ValidityStatus.EXPIRED -> "Istekla"
        ValidityStatus.UNKNOWN -> "—"
    }

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(180.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(primary, primaryContainer)
                    )
                )
                .padding(20.dp)
        ) {
            // Logos top-right
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.isic_logo),
                    contentDescription = stringResource(R.string.international_student_identity_card_logo_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(52.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.eyca_logo),
                    contentDescription = stringResource(R.string.logo_off_european_youth_card_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(36.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.coat_of_arms_of_serbia_small),
                    contentDescription = stringResource(R.string.coat_of_arms_of_serbia_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(28.dp).width(22.dp)
                )
            }

            // Name + faculty
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = if (surname.isBlank() && name.isBlank()) "Ime Prezime"
                           else "$surname $name".trim(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (faculty.isNotBlank()) {
                    Text(
                        text = faculty,
                        style = MaterialTheme.typography.bodySmall,
                        color = onPrimary.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index.isNotBlank()) {
                    Text(
                        text = index,
                        style = MaterialTheme.typography.bodySmall,
                        color = onPrimary.copy(alpha = 0.7f)
                    )
                }
            }

            // Bottom row: validity
            Row(
                modifier = Modifier.align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (validUntil.isNotBlank()) {
                    Text(
                        text = "Važi do: $validUntil",
                        style = MaterialTheme.typography.labelSmall,
                        color = onPrimary.copy(alpha = 0.8f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────
// Token summary (read-only)
// ────────────────────────────────────────────────────

@Composable
private fun TokenSummaryRow(remainingOnCard: SnapshotStateList<Int>) {
    val meals = listOf(
        Triple(stringResource(R.string.breakfast), remainingOnCard[0], Icons.Default.Coffee),
        Triple(stringResource(R.string.lunch), remainingOnCard[1], Icons.Filled.Restaurant),
        Triple(stringResource(R.string.dinner), remainingOnCard[2], Icons.Outlined.Fastfood),
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(meals.size) { i ->
            val (label, count, icon) = meals[i]
            TokenChip(label = label, count = count, icon = icon)
        }
    }
}

@Composable
private fun TokenChip(label: String, count: Int, icon: ImageVector) {
    val low = count <= 2
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (low) MaterialTheme.colorScheme.errorContainer
                             else MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (low) MaterialTheme.colorScheme.error
                       else MaterialTheme.colorScheme.secondary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (low) MaterialTheme.colorScheme.onErrorContainer
                        else MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (low) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.secondary
            )
        }
    }
}

// ────────────────────────────────────────────────────
// Section header
// ────────────────────────────────────────────────────

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
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
    validityStatus: ValidityStatus? = null
) {
    val statusColor = when (validityStatus) {
        ValidityStatus.VALID -> Color(0xFF4CAF50)
        ValidityStatus.EXPIRING_SOON -> Color(0xFFFFC107)
        ValidityStatus.EXPIRED -> MaterialTheme.colorScheme.error
        else -> null
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = statusColor ?: MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
        )
        if (statusColor != null && value.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = statusColor.copy(alpha = 0.12f)
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(50))
                            .background(statusColor)
                    )
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────
// Action buttons
// ────────────────────────────────────────────────────

@Composable
private fun ActionButtons(
    onShare: () -> Unit,
    onDiscard: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = onDiscard,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.discard))
            }
            FilledTonalButton(
                onClick = onShare,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = stringResource(R.string.share),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.share))
            }
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
    birthDialogState: DatePickerState
) {
    MenzaDatePickerDialog(
        onDismiss = { showBirthDialog.value = false },
        onConfirm = {
            dateofBirth.value = birthDialogState.selectedDateMillis?.convertMillisToDate() ?: ""
            showBirthDialog.value = false
        }
    ) {
        DatePicker(state = birthDialogState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssuedPicker(
    cardIssued: MutableState<String>,
    showIssuedDialog: MutableState<Boolean>,
    issuedState: DatePickerState
) {
    MenzaDatePickerDialog(
        onDismiss = { showIssuedDialog.value = false },
        onConfirm = {
            cardIssued.value = issuedState.selectedDateMillis?.convertMillisToDate() ?: ""
            showIssuedDialog.value = false
        }
    ) {
        DatePicker(state = issuedState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidPicker(
    cardValid: MutableState<String>,
    showValidDialog: MutableState<Boolean>,
    validState: DatePickerState
) {
    MenzaDatePickerDialog(
        onDismiss = { showValidDialog.value = false },
        onConfirm = {
            cardValid.value = validState.selectedDateMillis?.convertMillisToDate() ?: ""
            showValidDialog.value = false
        }
    ) {
        DatePicker(state = validState, colors = menzaDatePickerColors())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenzaDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    DatePickerDialog(
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(R.string.ok)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.discard)) }
        }
    ) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun menzaDatePickerColors() = DatePickerDefaults.colors(
    selectedDayContainerColor = MaterialTheme.colorScheme.primaryContainer,
    selectedDayContentColor = MaterialTheme.colorScheme.primary,
    selectedYearContainerColor = MaterialTheme.colorScheme.secondaryContainer,
    selectedYearContentColor = MaterialTheme.colorScheme.secondary,
    todayContentColor = MaterialTheme.colorScheme.tertiary,
    todayDateBorderColor = MaterialTheme.colorScheme.tertiaryContainer
)

// ────────────────────────────────────────────────────
// Utilities
// ────────────────────────────────────────────────────

fun Long.convertMillisToDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@convertMillisToDate
        val zoneOffset = get(Calendar.ZONE_OFFSET)
        val dstOffset = get(Calendar.DST_OFFSET)
        add(Calendar.MILLISECOND, -(zoneOffset + dstOffset))
    }
    val sdf = SimpleDateFormat("yyyy MMM dd", Locale.ENGLISH)
    return sdf.format(calendar.time)
}

// ────────────────────────────────────────────────────
// Preview
// ────────────────────────────────────────────────────

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewEditScreen() {
    MenzaNSTheme {
        val remainingOnCard = remember { mutableStateListOf(10, 2, 5, 0) }
        CardScreen(remainingOnCard, remember { SnackbarHostState() }, PaddingValues(0.dp))
    }
}
