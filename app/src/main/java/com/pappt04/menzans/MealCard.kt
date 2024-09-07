package com.pappt04.menzans

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme


data class MealData(var name: String, var price: Int, var remaining: Int)


@Composable
fun MealCard(meal: MealData) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column {
            Text(
                text = meal.name,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
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
                text = "Remaining: ${meal.remaining}",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
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
                    horizontalAlignment =Alignment.CenterHorizontally
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
                                Toast.makeText(
                                    context,
                                    "Welcome to Geeks for Geeks",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            },
                        ) {
                            Text("Minus")
                        }

                        Button(onClick = {
                            Toast.makeText(context, "Welcome to Geeks for Geeks", Toast.LENGTH_LONG)
                                .show()
                        }) {
                            Text("Plus")
                        }
                    }
                }
            }
        }

    }
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
        MealCard(MealData("Breakfast", 90, 1))
    }
}
