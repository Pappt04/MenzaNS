package com.pappt04.menzans

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.DummyData.datetypemonth
import com.pappt04.menzans.DummyData.engmonths
import java.util.Date

@Composable
fun StatisticsScreen(innerpadding: PaddingValues) {
    val context = LocalContext.current

    val read= readFromFile(context, engmonths[datetypemonth.format(Date()).toInt()-1])
    val splitStatisticsData=read.split(";")

    Column(modifier = Modifier
        .padding(innerpadding)
        .fillMaxWidth()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
//        Text(
//            test,
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .fillMaxSize()
//        )
        Card( elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally){
                WeekView(DummyData.dataweek) }
        }

    }
}
