package com.pappt04.menzans

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme


@Composable
fun MealCard(meal: MealData, remaining: Int, fileToSave: String, balance: MutableState<Int>) {
    var isExpanded by remember { mutableStateOf(false) }
    var currentlyRemaining by remember { mutableIntStateOf(remaining) }
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                text = meal.name,
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
                text = "${meal.price} din",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "Remaining: $currentlyRemaining",
                textAlign = TextAlign.Center,
                //fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
            )
            //TODO DAYS IN WEEK
            //TODO TWO BUTTONS TO ADD AND SUBTRACT
            AnimatedVisibility(
                isExpanded,
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                val dataprev = listOf('M', 'T', 'W', 'T', 'F', 'S', 'S')
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WeekView(dataprev)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (currentlyRemaining > 0) {
                                    balance.value += meal.price
                                    currentlyRemaining--
                                }
                                saveToFile(context, DummyData.FileNames[3], balance.value)
                                saveToFile(context, fileToSave, currentlyRemaining)
                            },
                        ) {
                            Text("Minus")
                        }
                        Button(
                            onClick = {
                                if (currentlyRemaining > 0) {
                                    currentlyRemaining--
                                }
                                saveToFile(context, fileToSave, currentlyRemaining)
                            },
                        ) {
                            Text("Consume")
                        }

                        Button(onClick = {
                            if (balance.value > meal.price) {
                                balance.value -= meal.price
                                currentlyRemaining++
                            }
                            saveToFile(context, DummyData.FileNames[3], balance.value)
                            saveToFile(context, fileToSave, currentlyRemaining)
                        }) {
                            Text("Plus")
                        }
                    }
                }
            }
        }

    }
}

fun saveToFile(context: Context, file: String, remaining: Int) {
    val s1 = remaining.toString()
    context.openFileOutput(file, Context.MODE_PRIVATE).use {
        it.write(s1.toByteArray())
    }
}

fun readFromFile(context: Context,file: String): String
{
    var s1=""
    context.openFileInput(file).bufferedReader().useLines { lines ->
        lines.fold("") { some, text ->
            s1="$some$text"
            s1
        }
    }
    return s1
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMealCard() {
    MenzaNSTheme {
        val counter = remember { mutableIntStateOf(500) }
        MealCard(MealData("Breakfast", 67, 7, 0, 9, 30), 6, DummyData.FileNames[0],counter)
    }
}
