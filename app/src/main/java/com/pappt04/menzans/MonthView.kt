package com.pappt04.menzans
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.*
import java.text.DateFormatSymbols
import java.time.DayOfWeek
import java.time.format.TextStyle

@Composable
fun CalendarMonthView(
    monthName: String,
    data: List<EatingStatisticsData>
) {
    val context= LocalContext.current

    val currentYear = LocalDate.now().year
    val month = Month.valueOf(monthName.uppercase(Locale.getDefault()))
    val yearMonth = YearMonth.of(currentYear, month)
    val startOfMonth = yearMonth.atDay(1)
    val totalDays = yearMonth.lengthOfMonth()
    val startDayOfWeekIndex = (startOfMonth.dayOfWeek.value % 7) // Adjust for Monday start
    val today = LocalDate.now()

    // Get localized names for days of the week
    val daysOfWeek = DayOfWeek.entries.map { dayOfWeek ->
        dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    // Generate the list of days
    val days = (1..totalDays).map { day -> yearMonth.atDay(day) }

    // Background color (same as the app's background color)
    val backgroundColor = MaterialTheme.colorScheme.background

    val selectedDay = remember { mutableIntStateOf(0) }

    val showDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(1.dp, Color.Gray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Day Headers using localized day names
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                daysOfWeek.forEach { day ->
                    Text(text = day, modifier = Modifier.weight(1f), maxLines = 1)
                }
            }

            // Calendar Days
            val calendarRows = mutableListOf<List<LocalDate?>>()
            val tempRow = mutableListOf<LocalDate?>()

            // Fill the initial gap days
            repeat(startDayOfWeekIndex) {
                tempRow.add(null)
            }

            // Fill calendar with actual days
            for (day in days) {
                if (tempRow.size == 7) {
                    calendarRows.add(tempRow.toList())
                    tempRow.clear()
                }
                tempRow.add(day)
            }
            // Add the last row
            if (tempRow.isNotEmpty()) {
                while (tempRow.size < 7) {
                    tempRow.add(null) // Fill remaining days with nulls
                }
                calendarRows.add(tempRow)
            }

            // Display rows
            calendarRows.forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    week.forEach { day ->
                        if (day != null) {
                            val isToday = day == today

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .clickable { selectedDay.intValue = day.dayOfMonth },
                                color = if (isToday) MaterialTheme.colorScheme.primary else backgroundColor
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Text(
                                        text = day.dayOfMonth.toString(),
                                        color = if (isToday) Color.White else MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).padding(4.dp))
                        }
                    }
                }
            }
        }
        AnimatedVisibility(
            selectedDay.intValue != 0,
            modifier =
            Modifier.run {
                animateContentSize(
                        animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            }
        ) {
            Column {
                HorizontalDivider(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = { showDialog.value = true
                              println(data) },
                    modifier =
                    Modifier.padding(10.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                ) { Text("Add meal") }
                if (showDialog.value)
                    AddMealDialog(
                        onDismissRequest = { showDialog.value = false },
                        context = context,
                        selectedDay.intValue
                    )
            }
        }
    }
}
