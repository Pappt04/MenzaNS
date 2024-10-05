package com.pappt04.menzans

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

@Composable
fun DayView(day: String, used: Int, selected: MutableState<Int>) {
    Surface(
        modifier = Modifier
            .padding(1.dp)
            .clickable { selected.value = day.toInt() },
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = day,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            when (used) {
                0 -> Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )

                1 -> Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
                2 -> Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )

                else -> Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )


            }
        }
    }
}

@Composable
fun EmptyDayView()
{
    Surface(
        modifier = Modifier
            .padding(1.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(2.dp)
    ){
        Column(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = "",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
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
fun WeekView(days: List<Uitext>, data: List<EatingStatisticsData>) {

    // TODO FIND A GOOD USAGE AND IMPLEMENTATION
    val week = getDatesOfWeek(true)
    val selected = remember { mutableIntStateOf(4) }
    LazyRow(
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(days) { day: Uitext ->
            var i=0
            for (meal in data) {
                if (meal.date in week)
                    i++
            }
            DayView(day.asString(context = LocalContext.current),i%4,selected)
        }
    }
}

fun convertStringtoDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(DummyData.datetypeall.toPattern())
    return LocalDate.parse(dateString, formatter)
}

fun getDatesOfWeek(includeToday: Boolean = true): List<LocalDate> {
    val today = LocalDate.now()

    // Find the previous Monday, excluding today if specified
    val monday =
        today.minusDays((if (includeToday) today.dayOfWeek.value - 1 else today.dayOfWeek.value).toLong())

    // Generate a list of dates from Monday to Sunday
    return (0..6).map { monday.plusDays(it.toLong()) }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewWeekView() {
    // val dataprev= listOf('M','T','W','T','F','S','S')
    // WeekView(dataprev)
}