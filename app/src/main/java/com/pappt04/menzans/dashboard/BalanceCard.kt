package com.pappt04.menzans.dashboard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import com.pappt04.menzans.animations.AnimatedNumber
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.FileDAO

@Composable
fun BalanceCard(balance: MutableIntState)
{
    var showBalanceDialog by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .padding(8.dp)
            .clickable {
                showBalanceDialog=true
            },
        colors = CardColors(MaterialTheme.colorScheme.primaryContainer,CardDefaults.cardColors().contentColor,CardDefaults.cardColors().disabledContainerColor,CardDefaults.cardColors().disabledContentColor)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.balance),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
                AnimatedNumber(number = balance,fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    fontSize = 42.sp,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = " "+stringResource(R.string.rsd),
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(2.dp)
                )
            }

        }
        if (showBalanceDialog) {
            BalanceDialog(
                onDismissRequest = {
                    showBalanceDialog = false
                },
                balance, LocalContext.current, DummyData.FileNames[3]
            )
        }
    }
}