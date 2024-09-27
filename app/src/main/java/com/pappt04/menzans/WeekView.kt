package com.pappt04.menzans

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme

@Composable
fun DayView(day: String) {
    Surface(
        modifier = Modifier
            .padding(1.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(2.dp)
    ) {
        Text(
            text = day,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview
@Composable
fun PreviewDayView() {
    MenzaNSTheme {
        //DayView('M')
    }
}

@Composable
fun WeekView(days: List<String>) {
    MenzaNSTheme {
        LazyRow(
            modifier = Modifier
                .padding(3.dp)
        ) {
            items(days) { day: String -> DayView(day) }
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
fun PreviewWeekView()
{
   // val dataprev= listOf('M','T','W','T','F','S','S')
   // WeekView(dataprev)
}