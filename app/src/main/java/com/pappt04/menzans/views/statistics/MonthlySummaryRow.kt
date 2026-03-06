package com.pappt04.menzans.views.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.data.consts.MealSample
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.ui.theme.Spacing

@Composable
fun MonthlySummaryRow(
    data: List<EatingStatisticsData>,
    onBudget: Boolean,
) {
    val (breakfast, lunch, dinner) = getMealCounts(data)
    val totalMeals = breakfast.toInt() + lunch.toInt() + dinner.toInt()

    val meals = MealSample.getMeals(onBudget)
    val totalSpent = breakfast.toInt() * meals[0].price +
        lunch.toInt() * meals[1].price +
        dinner.toInt() * meals[2].price

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.md, vertical = Spacing.sm),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.md),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            SummaryMetric(
                icon = { Icon(Icons.Outlined.RestaurantMenu, contentDescription = null) },
                value = totalMeals.toString(),
                label = stringResource(R.string.summary_tokens_used),
            )
            SummaryMetric(
                icon = { Icon(Icons.Outlined.AccountBalanceWallet, contentDescription = null) },
                value = "$totalSpent ${stringResource(R.string.rsd)}",
                label = stringResource(R.string.summary_rsd_spent),
            )
        }
    }
}

@Composable
private fun SummaryMetric(
    icon: @Composable () -> Unit,
    value: String,
    label: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        icon()
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
        )
    }
}
