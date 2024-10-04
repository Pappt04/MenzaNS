package com.pappt04.menzans

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat

@Composable
fun WelcomeDialog(onDismissRequest: () -> Unit, context: Context) {
    var hasPermission by remember {
        mutableStateOf(false)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    Dialog(onDismissRequest = onDismissRequest)
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
            //.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.welcome_title),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(20.dp)
                )
                Text(
                    stringResource(R.string.welcome_message),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(15.dp)
                )
                Button(onClick = {
                    for(per in DummyData.permissionsNeeded)
                        permissionLauncher.launch(per)
                },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    Text("Request permissions")
                }
            }
        }
    }
}