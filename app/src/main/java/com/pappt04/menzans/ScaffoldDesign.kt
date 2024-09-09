package com.pappt04.menzans

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldDesign(meals: List<MealData>, remainingOnCard: Array<Int>) {
    var showBalanceDialog: Boolean by remember { mutableStateOf(false) }
    var currentBalance: Int by remember { mutableIntStateOf(remainingOnCard[3]) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("MenzaNS")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    text = "Balance: ${currentBalance} rsd",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBalanceDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MealContainer(meals, remainingOnCard)
            if (showBalanceDialog) {
                BalanceDialog(
                    onDismissRequest = { showBalanceDialog = false
                        currentBalance = balanceFromDialog
                    },
                    currentBalance, LocalContext.current, DummyData.FileNames[3]
                )

            }
        }
    }
}

@Preview
@Composable
fun PreviewScaffold() {
    MenzaNSTheme {
        ScaffoldDesign(DummyData.MealSample, DummyData.RemainingONCardSample)
    }
}