package com.pappt04.menzans.views.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.models.MealPeriod
import com.pappt04.menzans.models.isActive
import com.pappt04.menzans.viewmodels.MenuViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MenuTab() {
    val viewModel: MenuViewModel = koinViewModel()
    val weekMenu by viewModel.weekMenu.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDayMenu by viewModel.selectedDayMenu.collectAsState()
    val isLoading by viewModel.weekLoading.collectAsState()
    val error by viewModel.weekError.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchWeekMenu()
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = error ?: stringResource(R.string.menu_load_error),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        weekMenu != null -> {
            WeekMenuContent(
                weekMenu = weekMenu!!,
                selectedDate = selectedDate,
                selectedDayMenu = selectedDayMenu,
                onDateSelected = viewModel::selectDate,
            )
        }
    }
}

@Composable
private fun WeekMenuContent(
    weekMenu: Map<String, DayMenu>,
    selectedDate: String?,
    selectedDayMenu: DayMenu?,
    onDateSelected: (String) -> Unit,
) {
    // Computed once per composition entry — date only changes at midnight
    val todayKey = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date()) }

    // Re-sorted only when the map identity changes, not on every recompose
    val sortedDates = remember(weekMenu) { weekMenu.keys.sortedWith(MenuViewModel.dateKeyComparator) }

    val listState = rememberLazyListState()
    val selectedIndex = remember(sortedDates, selectedDate) { sortedDates.indexOf(selectedDate) }
    LaunchedEffect(selectedIndex) {
        if (selectedIndex >= 0) listState.animateScrollToItem(selectedIndex)
    }

    // Calendar.getInstance() is called once here instead of 3× per recompose
    val breakfastActive = remember { MealPeriod.BREAKFAST.isActive() }
    val lunchActive = remember { MealPeriod.LUNCH.isActive() }
    val dinnerActive = remember { MealPeriod.DINNER.isActive() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))

        // Day selector
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(sortedDates, key = { it }) { date ->
                val dayMenu = weekMenu[date]
                DayChip(
                    date = date,
                    dayName = dayMenu?.day?.take(3) ?: date.take(5),
                    isSelected = date == selectedDate,
                    isToday = date == todayKey,
                    onClick = { onDateSelected(date) },
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(4.dp))

        // Meal sections
        if (selectedDayMenu != null) {
            val isToday = selectedDate == todayKey
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MealSection(
                    title = stringResource(R.string.breakfast),
                    icon = Icons.Default.Coffee,
                    items = selectedDayMenu.breakfast,
                    isCurrentPeriod = isToday && breakfastActive,
                )
                MealSection(
                    title = stringResource(R.string.lunch),
                    icon = Icons.Filled.Restaurant,
                    items = selectedDayMenu.lunch,
                    isCurrentPeriod = isToday && lunchActive,
                )
                MealSection(
                    title = stringResource(R.string.dinner),
                    icon = Icons.Outlined.Fastfood,
                    items = selectedDayMenu.dinner,
                    isCurrentPeriod = isToday && dinnerActive,
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.menu_day_unavailable),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun DayChip(
    date: String,
    dayName: String,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp),
            ) {
                Text(
                    text = dayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                )
                Text(
                    text = date.take(5),
                    style = MaterialTheme.typography.labelSmall,
                )
                if (isToday) {
                    Text(
                        text = stringResource(R.string.today),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        },
    )
}

@Composable
private fun MealSection(
    title: String,
    icon: ImageVector,
    items: List<String>?,
    isCurrentPeriod: Boolean,
) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isCurrentPeriod)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCurrentPeriod)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                if (isCurrentPeriod) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = stringResource(R.string.now_badge),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Items
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 10.dp))
                    if (items.isNullOrEmpty()) {
                        Text(
                            text = stringResource(R.string.no_meal_data),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items.forEach { item ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = item,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
