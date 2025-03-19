package com.pappt04.menzans.appui.dashboard


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.appui.animations.AnimatedNumber
import com.pappt04.menzans.data.consts.DummyData.datetypeclock
import com.pappt04.menzans.data.consts.DummyData.engmonths
import com.pappt04.menzans.data.EatingStatisticsData
import com.pappt04.menzans.data.MealData
import com.pappt04.menzans.data.StatisticsFileDAO
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date


@SuppressLint("DefaultLocale")
@Composable
fun DetailedMealCard(meal: MealData, remaining: MutableIntState, balance: MutableState<Int>, onClicked: () -> Unit, noFunds:() -> Unit, onChanged:() -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable {onClicked()}
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = meal.name.asString(context),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = String.format(
                    "%02d:%02d-%02d:%02d",
                    meal.start_hour,
                    meal.start_minute,
                    meal.end_hour,
                    meal.end_minute
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "${meal.price} rsd",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = stringResource(R.string.remaining) + ": ",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                )
                AnimatedNumber(remaining, style=MaterialTheme.typography.titleLarge)
            }


                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (remaining.value > 0) {
                                    balance.value += meal.price
                                    remaining.value--
                                }
                                onChanged()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Remove,
                                contentDescription = null,
                            )
                        }
                        Button(
                            onClick = {
                                if (remaining.value > 0) {
                                    remaining.value--

                                    scope.launch {
                                        val statisticsMeal = EatingStatisticsData(
                                            LocalDate.now(),
                                            datetypeclock.format(Date()),
                                            datetypeclock.format(Date()),
                                            meal.name
                                        )
                                        val fdao= StatisticsFileDAO(context, engmonths[LocalDate.now().monthValue-1])
                                        fdao.appendToStatisticsFile(statisticsMeal)


                                    }
                                }
                                onChanged()
                            },
                        ) {
                            Text(stringResource(R.string.consume))
                        }

                        Button(onClick = {
                            if (balance.value > meal.price) {
                                balance.value -= meal.price
                                remaining.value++
                                onChanged()
                            } else
                            {
                                noFunds()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                            )
                        }
                    }
                }

        }

    }
}