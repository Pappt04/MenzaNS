package com.pappt04.menzans.views.settings

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.ContextCompat
import com.pappt04.menzans.R
import com.pappt04.menzans.data.local.FileContainer
import com.pappt04.menzans.data.local.FileDAO
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.data.consts.DummyData
import com.pappt04.menzans.data.consts.DummyData.permissionsNeeded
import com.pappt04.menzans.data.local.datastore.SettingsDataStoreManager
import com.pappt04.menzans.models.SettingsPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SettingsScreen(
    innerpadding: PaddingValues,
    settingsdatamanager: SettingsDataStoreManager,
    darkTheme: MutableState<Boolean>,
    materialyoutheme: MutableState<Boolean>,
    onBudget: MutableState<Boolean>
) {

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val tokenwarning = remember { mutableFloatStateOf(0f) }

    var settingprefs = SettingsPreferences(
        darktheme = darkTheme.value,
        materialyoutheme = materialyoutheme.value,
        budget = onBudget.value,
        tokenwarning = tokenwarning.floatValue.roundToInt()
    )

    LaunchedEffect(key1 = Unit) {
        settingprefs = settingsdatamanager.getFromDataStore().first()
        tokenwarning.floatValue=settingprefs.tokenwarning.toFloat()
    }

    LazyColumn(
        modifier = Modifier
            .padding(innerpadding)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        item {
            DisclaimerCard(context)
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(10.dp))
        }
        item {
            LanguageChanger(context)
        }
        item {
            SettingSwitch(
                darkTheme,
                stringResource(R.string.use_dark_theme),
                FileContainer.FileDarkThemeEnabled
            )
            {
                settingprefs.darktheme=darkTheme.value
                scope.launch {
                    settingsdatamanager.saveToDataStore(
                        settingsData = settingprefs
                    )
                }
            }
        }
        item {
            SettingSwitch(
                materialyoutheme,
                stringResource(R.string.use_materialyou_theme),
                FileContainer.FileMaterialYouEnabled
            )
            {
                settingprefs.materialyoutheme=materialyoutheme.value
                scope.launch {
                    settingsdatamanager.saveToDataStore(
                        settingsData = settingprefs
                    )
                }
            }
        }
        item {
            PriceSwitcher(onBudget)
            {
                settingprefs.budget=onBudget.value
                scope.launch {
                    settingsdatamanager.saveToDataStore(
                        settingsData = settingprefs
                    )
                }
            }
        }
        item {
            TokenLimitSlider(tokenwarning)
            {
                settingprefs.tokenwarning=tokenwarning.floatValue.toInt()
                scope.launch {
                    settingsdatamanager.saveToDataStore(
                        settingsData = settingprefs
                    )
                }
            }
        }
        item { HorizontalDivider(modifier = Modifier.padding(10.dp)) }
        items(permissionsNeeded) { permission ->
            var i: Int = 0
            for (p in permissionsNeeded) {
                if (p == permission) {
                    break
                } else {
                    i++
                }
            }
            PermissionSwitch(context, DummyData.permissionExplanations[i].explanation, permission)
        }

    }
}



@Composable
fun PermissionSwitch(context: Context, permissionName: Uitext, permissionType: String) {
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
        Column(modifier = Modifier.padding(20.dp)) {
            Row() {
                Text(
                    stringResource(R.string.request_permission, permissionType.split(".").last()),
                    fontWeight = FontWeight.SemiBold,
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
            HorizontalDivider()
            Text(permissionName.asString(context))
        }
    }
}


@Composable
fun DisclaimerCard(context: Context) {
    Card(
        modifier = Modifier
            .padding(8.dp)
    ) {
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

//@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
//@Composable
//fun PreviewSettingsScreen() {
//    MenzaNSTheme {
//        val dark = remember { mutableStateOf(false) }
//        SettingsScreen(PaddingValues(20.dp), dark, dark, dark)
//    }
//}
