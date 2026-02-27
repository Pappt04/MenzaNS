package com.pappt04.menzans.views.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.viewmodels.MenuViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

private enum class MealPeriod(
    val label: String,
    val icon: ImageVector,
    val startH: Int,
    val startM: Int,
    val endH: Int,
    val endM: Int
) {
    BREAKFAST("Doručak", Icons.Default.Coffee, 7, 0, 9, 30),
    LUNCH("Ručak", Icons.Filled.Restaurant, 11, 0, 15, 0),
    DINNER("Večera", Icons.Outlined.Fastfood, 17, 0, 20, 30);

    fun minuteStart() = startH * 60 + startM
    fun minuteEnd() = endH * 60 + endM
}

private fun currentOrNextPeriod(): MealPeriod {
    val cal = Calendar.getInstance()
    val now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    // Return active period if within its window, otherwise the next upcoming one
    return MealPeriod.entries.firstOrNull { now < it.minuteEnd() } ?: MealPeriod.BREAKFAST
}

private fun MealPeriod.itemsFrom(menu: DayMenu): List<String> = when (this) {
    MealPeriod.BREAKFAST -> menu.breakfast
    MealPeriod.LUNCH -> menu.lunch
    MealPeriod.DINNER -> menu.dinner
}

private fun MealPeriod.isActive(): Boolean {
    val cal = Calendar.getInstance()
    val now = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
    return now in minuteStart() until minuteEnd()
}

@Composable
fun TodayMenuCard(viewModel: MenuViewModel = koinViewModel()) {
    val menu by viewModel.menu.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTodayMenu()
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        when {
            isLoading -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Text("Učitavanje jelovnika…", style = MaterialTheme.typography.bodyMedium)
            }

            error != null -> Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Jelovnik nije dostupan",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            menu != null -> MenuCardContent(menu!!)
        }
    }
}

@Composable
private fun MenuCardContent(menu: DayMenu) {
    val period = remember { currentOrNextPeriod() }
    val items = period.itemsFrom(menu)
    val active = remember { period.isActive() }

    var expanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(12.dp)) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = period.icon,
                contentDescription = null,
                tint = if (active) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (active) period.label else "Sledeće: ${period.label}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (active) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (menu.day.isNotBlank()) {
                    Text(
                        text = menu.day,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Skupi" else "Proširi",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))
                if (items.isEmpty()) {
                    Text(
                        text = "Nema podataka za ovaj obrok",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
