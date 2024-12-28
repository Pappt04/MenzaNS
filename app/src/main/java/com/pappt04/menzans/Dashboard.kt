package com.pappt04.menzans

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardDesign(
    meals: List<MealData>,
    remainingOnCard: SnapshotStateList<Int>,
) {
    val mealValueList = remember {
        MutableList(3) { index ->
            mutableIntStateOf(remainingOnCard[index])
        }
    }
    var showBalanceDialog: Boolean by remember { mutableStateOf(false) }
    val balance = remember { mutableIntStateOf(remainingOnCard[3]) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.app_name),
                        fontWeight= FontWeight.Bold,)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            //drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu_description)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.balance)+": ${balance.intValue} rsd",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showBalanceDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp)
            ) {
                var i=0
                items(meals) { meal: MealData ->
                    val index = remember { mutableIntStateOf(i) }

                    MealCard(meal,mealValueList[index.intValue],DummyData.FileNames[index.intValue],balance)

                    remainingOnCard[index.intValue] = mealValueList[index.intValue].intValue
                    remainingOnCard[3]=balance.intValue
                    i++
                    i %= 3
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
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewScaffold() {
    MenzaNSTheme {
//        ScaffoldDesign(
//            DummyData.MealSample,
//            DummyData.RemainingONCardSample,
//            drawerState = DrawerState(false),
//            scope =
//            )
    }
}