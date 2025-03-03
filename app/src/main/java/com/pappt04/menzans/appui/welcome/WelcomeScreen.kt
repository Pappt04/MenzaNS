package com.pappt04.menzans.appui.welcome

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.R
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.geolocation.GeofenceManager

@Composable
fun WelcomeScreen(onCompleted:() -> Unit,innerpadding: PaddingValues)
{
    val context= LocalContext.current
    var selectedPermissionIndex by remember { mutableIntStateOf(0) }

    var hasPermission by remember {
        mutableStateOf(false)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    Column(
        modifier = Modifier
            .padding(innerpadding),
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
        HorizontalDivider()
        Text(
            DummyData.permissionsExplanations[selectedPermissionIndex].asString(context = LocalContext.current),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(15.dp)
        )
        Text(
            "$selectedPermissionIndex/${DummyData.permissionsNeeded.size}"
        )
        Button(onClick = {
            permissionLauncher.launch(DummyData.permissionsNeeded[selectedPermissionIndex++])

            if(selectedPermissionIndex == DummyData.permissionsNeeded.size)
            {
                onCompleted()

                val geofenceManager = GeofenceManager(context)

                for (geofence in DummyData.LANDMARK_DATA) {
                    geofenceManager.addGeofence(
                        geofence.key,
                        geofence.location,
                        geofence.radiusInMeters,
                        geofence.expirationTimeInMillis
                    )
                }
                geofenceManager.registerGeofence()
            }
        },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f)
        ) {
            Text("Request permissions")
        }
    }

}