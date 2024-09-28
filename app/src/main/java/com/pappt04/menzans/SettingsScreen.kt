package com.pappt04.menzans

import android.Manifest
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat


@Composable
fun SettingsScreen(innerpadding: PaddingValues, darkTheme: MutableState<Boolean>) {
    val context = LocalContext.current
    val permissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )
    }
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
            LanguageChanger(context)
        }
        item {
            DarkThemeSwitcher(context, darkTheme)
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

                    saveToFile(context,DummyData.FileDarkThemeEnabled,dark)
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChanger(context: Context) {
    var isExpanded by remember { mutableStateOf(false) }

    val localeOptions = mapOf(
        R.string.en to "en",
        R.string.hu to "hu",
        R.string.sr to "sr",
    ).mapKeys { stringResource(it.key) }

    var selectedText by remember { mutableStateOf("english") }
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                stringResource(R.string.change_your_language),
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
                    .weight(3f)
            )
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
                modifier = Modifier
                    .weight(2f)
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = stringResource(R.string.language),
                    onValueChange = {},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }) {
                    localeOptions.keys.forEach { selectionLocale ->
                        DropdownMenuItem(
                            onClick = {
                                isExpanded = false
                                // set app locale given the user's selected locale
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(
                                        localeOptions[selectionLocale]
                                    )
                                )
                            },
                            text = { Text(selectionLocale) }
                        )
                    }
                }
            }
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
        //SettingsScreen(PaddingValues(20.dp),false)
    }
}
