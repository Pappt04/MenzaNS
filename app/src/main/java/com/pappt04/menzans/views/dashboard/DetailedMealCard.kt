package com.pappt04.menzans.views.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.ui.theme.Spacing
import com.pappt04.menzans.views.common.AnimatedNumber
import com.pappt04.menzans.data.consts.CalendarData.timeFormat
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.models.MealData
import java.time.LocalDate
import java.util.Date

@SuppressLint("DefaultLocale")
@Composable
fun DetailedMealCard(
    meal: MealData,
    remaining: MutableIntState,
    balance: MutableState<Int>,
    onClicked: () -> Unit,
    noFunds: () -> Unit,
    onChanged: () -> Unit,
    onConsumeMeal: (EatingStatisticsData) -> Unit,
) {
    val context = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
            .clickable { onClicked() },
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Text(
                text = meal.name.asString(context),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(Spacing.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = String.format(
                        "%02d:%02d – %02d:%02d",
                        meal.start_hour,
                        meal.start_minute,
                        meal.end_hour,
                        meal.end_minute,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${meal.price} ${stringResource(R.string.rsd)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${stringResource(R.string.remaining)}: ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                AnimatedNumber(remaining, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = {
                        if (remaining.intValue > 0) {
                            balance.value += meal.price
                            remaining.intValue--
                        }
                        onChanged()
                    },
                ) {
                    Icon(imageVector = Icons.Outlined.Remove, contentDescription = stringResource(R.string.subtract))
                }
                Button(
                    onClick = {
                        if (remaining.intValue > 0) {
                            remaining.intValue--
                            val statisticsMeal = EatingStatisticsData(
                                LocalDate.now(),
                                timeFormat.format(Date()),
                                timeFormat.format(Date()),
                                meal.name,
                            )
                            onConsumeMeal(statisticsMeal)
                        }
                        onChanged()
                    },
                ) {
                    Text(stringResource(R.string.consume))
                }
                OutlinedButton(
                    onClick = {
                        if (balance.value > meal.price) {
                            balance.value -= meal.price
                            remaining.intValue++
                            onChanged()
                        } else {
                            noFunds()
                        }
                    },
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add))
                }
            }
        }
    }
}
