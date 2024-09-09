package com.pappt04.menzans

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme

@Composable
fun MealContainer(meals: List<MealData>,remainingOnCard: Array<Int>) {
    MenzaNSTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)
        ) {
            var i=0
            items(meals) { meal: MealData -> MealCard(meal,remainingOnCard[i],DummyData.FileNames[i])
                i++
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
fun PreviewMealContainer() {
    MenzaNSTheme {
        MealContainer(DummyData.MealSample,DummyData.RemainingONCardSample)
    }
}
