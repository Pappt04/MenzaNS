package com.pappt04.menzans.settings

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.ContextCompat
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.core.os.LocaleListCompat
import com.pappt04.menzans.R
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.DummyData.permissionsNeeded
import com.pappt04.menzans.data.FileDAO


@Composable
fun SettingsScreen(innerpadding: PaddingValues, darkTheme: MutableState<Boolean>, materialyoutheme: MutableState<Boolean>, onBudget: MutableState<Boolean>) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .padding(innerpadding)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        item{
            DisclaimerCard(context)
        }
        item {
            PriceSwitcher(context,onBudget)
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
        }
        item {
            LanguageChanger(context)
        }

        item {
            DarkThemeSwitcher(context, darkTheme)
            Log.i("DarkTheme status","$darkTheme")
        }
        item {
            MaterialThemeSwitcher(context,materialyoutheme)
        }
        item { HorizontalDivider(modifier = Modifier.padding(10.dp)) }
        item {
            this@LazyColumn.items(permissionsNeeded) { permission: String ->
                PermissionSwitch(context, permission)
            }
        }

    }
}

@Composable
fun PermissionSwitch(context: Context, permissionType: String) {
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permissionType
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
        }
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(R.string.request_permission, permissionType.split(".").last()),
                modifier = Modifier.weight(4f)
            )
            Switch(
                checked = hasPermission,
                onCheckedChange = {
                    hasPermission = it
                    if (hasPermission) {
                        permissionLauncher.launch(permissionType)

                    }
                },
                thumbContent = if (hasPermission) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun DarkThemeSwitcher(context: Context, darkTheme: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                stringResource(R.string.use_dark_theme),
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ),
                modifier = Modifier
                    .weight(4f)
            )
            Switch(
                checked = darkTheme.value,
                onCheckedChange = {
                    darkTheme.value = it
                    var dark=0
                    if(darkTheme.value) {
                        dark = 1
                    }
                    else {
                        dark = 0
                    }
                    var fdao= FileDAO(context, DummyData.FileDarkThemeEnabled)
                    fdao.saveToFile(dark,false)
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}


@Composable
fun MaterialThemeSwitcher(context: Context, materialyoutheme: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(
                stringResource(R.string.use_materialyou_theme),
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeight = 2.5.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                ),
                modifier = Modifier
                    .weight(4f)
            )
            Switch(
                checked = materialyoutheme.value,
                onCheckedChange = {
                    materialyoutheme.value = it
                    var matyou=0
                    matyou = if(materialyoutheme.value) {
                        1
                    } else {
                        0
                    }
                    val fdao= FileDAO(context, DummyData.FileMaterialYouEnabled)
                    fdao.saveToFile(matyou)
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}


@Composable
fun DisclaimerCard(context: Context)
{
    Card(modifier = Modifier
        .padding(8.dp)) {
        Text(
            text = stringResource(R.string.disclaimer_this_is_a_student_project_with_no_affiliation_with_the_university_of_novi_sad),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewSettingsScreen() {
    MenzaNSTheme {
        val dark = remember { mutableStateOf(false) }
        SettingsScreen(PaddingValues(20.dp),dark,dark,dark)
    }
}
