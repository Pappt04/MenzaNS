package com.pappt04.menzans.appui.dashboard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import com.pappt04.menzans.appui.animations.AnimatedNumber
import com.pappt04.menzans.data.MinuteTicker
import com.pappt04.menzans.data.getLineGraph
import com.pappt04.menzans.data.getWaitTime

@Composable
fun WaitTimeCard(waittime: MutableIntState, onFetch:() -> Unit) {
    val context = LocalContext.current
    val trajectory = remember { mutableIntStateOf(0) }
    val precision = remember { mutableStateOf(0.00) }

    val bscolor = when(trajectory.intValue)
    {
        -1 -> Color.Green
        1 -> Color.Red
        else -> Color.Gray
    }

    val arrowIcon = when (trajectory.intValue) {
        1 -> Icons.Filled.KeyboardArrowUp
        -1 -> Icons.Filled.KeyboardArrowDown
        else -> Icons.Filled.Remove
    }

    MinuteTicker {
        Log.d("WAIT_TIME", "Refresh request sent")
        getWaitTime(context) { wt ->
            if (wt != null) {
                val temp = waittime.intValue
                waittime.intValue = wt.waittime.toInt()

                precision.value=wt.precision.toDouble()

                if (temp == 999) {
                    trajectory.intValue = wt.trajectory.toInt()
                } else if (temp < waittime.intValue) {
                    trajectory.intValue = 1
                } else if (temp > waittime.intValue) {
                    trajectory.intValue = -1
                } else {
                    trajectory.intValue = 0
                }
            }
        }
    }

    Card(
        colors = CardColors(MaterialTheme.colorScheme.tertiaryContainer,
            CardDefaults.cardColors().contentColor,
            CardDefaults.cardColors().disabledContainerColor,
            CardDefaults.cardColors().disabledContentColor),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(8.dp)
            .clickable {
                Log.d("WAIT_TIME", "Refresh request sent")
                Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
                getWaitTime(context) { wt ->
                    if (wt != null) {
                        val temp = waittime.intValue
                        waittime.intValue = wt.waittime.toInt()

                        if (temp == 999) {
                            trajectory.intValue = wt.trajectory.toInt()
                        } else if (temp < waittime.intValue) {
                            trajectory.intValue = 1
                        } else if (temp > waittime.intValue) {
                            trajectory.intValue = -1
                        } else {
                            trajectory.intValue = 0
                        }
                    }
                }
            },
        border = BorderStroke(1.dp,bscolor)
    ) {

        Column {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.wait_time),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                )
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {


                    if(waittime.intValue != 999) {
                        Text(
                            text = "~",
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        AnimatedNumber(
                            waittime, fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                    else
                    {
                        Text(
                            text = "?",
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 42.sp,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    Text(
                        text = " min",
                        fontSize = 40.sp,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(2.dp)
                    )
                        Icon(
                            imageVector = arrowIcon,
                            contentDescription = if (trajectory.intValue == -1) "Downward Trend" else "Upward Trend",
                            tint = bscolor
                        )

                }
                Text(stringResource(R.string.precision, precision.value.toString()), modifier = Modifier.align(Alignment.End))
            }
        }

    }

}