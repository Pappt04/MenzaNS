package com.pappt04.menzans.views.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import com.pappt04.menzans.ui.theme.Spacing
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.pappt04.menzans.R
import com.pappt04.menzans.data.consts.PermissionData
import com.pappt04.menzans.data.consts.PermissionData.permissionsNeeded
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.viewmodels.MainViewModel
import com.pappt04.menzans.views.common.AnimatedAppearance
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SettingsScreen(
    innerpadding: PaddingValues,
    mainViewModel: MainViewModel,
) {
    val context = LocalContext.current

    val state by mainViewModel.uiState.collectAsState()
    val darkTheme = remember { mutableStateOf(state.darkTheme) }
    val materialyoutheme = remember { mutableStateOf(state.materialYouTheme) }
    val breakfastTokenWarning = remember { mutableFloatStateOf(state.breakfastTokenWarning.toFloat()) }
    val lunchTokenWarning = remember { mutableFloatStateOf(state.lunchTokenWarning.toFloat()) }
    val dinnerTokenWarning = remember { mutableFloatStateOf(state.dinnerTokenWarning.toFloat()) }
    val onBudget = remember { mutableStateOf(state.onBudgetPricing) }
    val geofenceEnabled = remember { mutableStateOf(state.geofenceEnabled) }
    val eatingSpeedThreshold = remember { mutableFloatStateOf(state.eatingSpeedThreshold.toFloat()) }
    val autoDeduct = remember { mutableStateOf(state.autoDeduct) }
    val breakfastNotifyThreshold = remember { mutableFloatStateOf(state.breakfastNotifyThreshold.toFloat()) }
    val lunchNotifyThreshold = remember { mutableFloatStateOf(state.lunchNotifyThreshold.toFloat()) }
    val dinnerNotifyThreshold = remember { mutableFloatStateOf(state.dinnerNotifyThreshold.toFloat()) }

    LaunchedEffect(state.darkTheme) { darkTheme.value = state.darkTheme }
    LaunchedEffect(state.materialYouTheme) { materialyoutheme.value = state.materialYouTheme }
    LaunchedEffect(state.breakfastTokenWarning) { breakfastTokenWarning.floatValue = state.breakfastTokenWarning.toFloat() }
    LaunchedEffect(state.lunchTokenWarning) { lunchTokenWarning.floatValue = state.lunchTokenWarning.toFloat() }
    LaunchedEffect(state.dinnerTokenWarning) { dinnerTokenWarning.floatValue = state.dinnerTokenWarning.toFloat() }
    LaunchedEffect(state.onBudgetPricing) { onBudget.value = state.onBudgetPricing }
    LaunchedEffect(state.geofenceEnabled) { geofenceEnabled.value = state.geofenceEnabled }
    LaunchedEffect(state.eatingSpeedThreshold) { eatingSpeedThreshold.floatValue = state.eatingSpeedThreshold.toFloat() }
    LaunchedEffect(state.autoDeduct) { autoDeduct.value = state.autoDeduct }
    LaunchedEffect(state.breakfastNotifyThreshold) { breakfastNotifyThreshold.floatValue = state.breakfastNotifyThreshold.toFloat() }
    LaunchedEffect(state.lunchNotifyThreshold) { lunchNotifyThreshold.floatValue = state.lunchNotifyThreshold.toFloat() }
    LaunchedEffect(state.dinnerNotifyThreshold) { dinnerNotifyThreshold.floatValue = state.dinnerNotifyThreshold.toFloat() }

    LazyColumn(
        modifier =
            Modifier
                .padding(innerpadding)
                .fillMaxWidth()
                .fillMaxHeight(),
    ) {
        // Appearance
        item {
            AnimatedAppearance(delay = 50.milliseconds) {
                SectionHeader(Icons.Outlined.Palette, stringResource(R.string.settings_section_appearance))
            }
        }
        item {
            LanguageChanger()
        }
        item {
            SettingSwitch(
                darkTheme,
                stringResource(R.string.use_dark_theme),
            ) {
                mainViewModel.updateDarkTheme(darkTheme.value)
            }
        }
        item {
            SettingSwitch(
                materialyoutheme,
                stringResource(R.string.use_materialyou_theme),
            ) {
                mainViewModel.updateMaterialYou(materialyoutheme.value)
            }
        }

        // Price & Tokens
        item {
            AnimatedAppearance(delay = 100.milliseconds) {
                SectionHeader(Icons.Outlined.Payments, stringResource(R.string.settings_section_price_tokens))
            }
        }
        item {
            PriceSwitcher(onBudget) {
                mainViewModel.updateBudgetPricing(onBudget.value)
            }
        }
        item {
            TokenLimitSlider(
                label = stringResource(R.string.token_warning_breakfast),
                sliderpos = breakfastTokenWarning,
            ) {
                mainViewModel.updateBreakfastTokenWarning(breakfastTokenWarning.floatValue.roundToInt())
            }
        }
        item {
            TokenLimitSlider(
                label = stringResource(R.string.token_warning_lunch),
                sliderpos = lunchTokenWarning,
            ) {
                mainViewModel.updateLunchTokenWarning(lunchTokenWarning.floatValue.roundToInt())
            }
        }
        item {
            TokenLimitSlider(
                label = stringResource(R.string.token_warning_dinner),
                sliderpos = dinnerTokenWarning,
            ) {
                mainViewModel.updateDinnerTokenWarning(dinnerTokenWarning.floatValue.roundToInt())
            }
        }

        // Detection
        item {
            AnimatedAppearance(delay = 150.milliseconds) {
                SectionHeader(Icons.Outlined.MyLocation, stringResource(R.string.settings_section_detection))
            }
        }
        item {
            SettingSwitch(
                geofenceEnabled,
                stringResource(R.string.enable_auto_detection),
            ) {
                mainViewModel.updateGeofenceEnabled(geofenceEnabled.value)
            }
        }
        item {
            EatingThresholdSlider(eatingSpeedThreshold) {
                mainViewModel.updateEatingSpeedThreshold(eatingSpeedThreshold.floatValue.roundToInt())
            }
        }
        item {
            SettingSwitch(
                autoDeduct,
                stringResource(R.string.auto_deduct_token),
            ) {
                mainViewModel.updateAutoDeduct(autoDeduct.value)
            }
        }

        // Notifications
        item {
            AnimatedAppearance(delay = 200.milliseconds) {
                SectionHeader(Icons.Outlined.Notifications, stringResource(R.string.settings_section_notifications))
            }
        }
        item {
            MealThresholdSlider(
                label = stringResource(R.string.notify_threshold_breakfast),
                sliderpos = breakfastNotifyThreshold,
            ) {
                mainViewModel.updateBreakfastNotifyThreshold(breakfastNotifyThreshold.floatValue.roundToInt())
            }
        }
        item {
            MealThresholdSlider(
                label = stringResource(R.string.notify_threshold_lunch),
                sliderpos = lunchNotifyThreshold,
            ) {
                mainViewModel.updateLunchNotifyThreshold(lunchNotifyThreshold.floatValue.roundToInt())
            }
        }
        item {
            MealThresholdSlider(
                label = stringResource(R.string.notify_threshold_dinner),
                sliderpos = dinnerNotifyThreshold,
            ) {
                mainViewModel.updateDinnerNotifyThreshold(dinnerNotifyThreshold.floatValue.roundToInt())
            }
        }

        // Permissions
        item {
            AnimatedAppearance(delay = 250.milliseconds) {
                SectionHeader(Icons.Outlined.Security, stringResource(R.string.settings_section_permissions))
            }
        }
        items(permissionsNeeded) { permission ->
            var i = 0
            for (p in permissionsNeeded) {
                if (p == permission) {
                    break
                } else {
                    i++
                }
            }
            PermissionSwitch(context, PermissionData.permissionExplanations[i].explanation, permission)
        }

        // Disclaimer at the bottom
        item { HorizontalDivider(modifier = Modifier.padding(Spacing.md)) }
        item {
            DisclaimerCard()
        }
    }
}

@Composable
fun PermissionSwitch(
    context: Context,
    permissionName: Uitext,
    permissionType: String,
) {
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permissionType,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasPermission = isGranted
            },
        )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier =
            Modifier
                .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(Spacing.md)) {
            Row {
                Text(
                    stringResource(R.string.request_permission, permissionType.split(".").last()),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(4f),
                )
                Switch(
                    checked = hasPermission,
                    onCheckedChange = { checked ->
                        if (checked) {
                            permissionLauncher.launch(permissionType)
                        } else {
                            // Android does not allow apps to revoke permissions programmatically.
                            // Send the user to the system app settings page to do it manually.
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        }
                    },
                    thumbContent =
                        if (hasPermission) {
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
                    modifier =
                        Modifier
                            .weight(1f),
                )
            }
            HorizontalDivider()
            Text(permissionName.asString(context))
        }
    }
}

@Composable
fun DisclaimerCard() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier =
            Modifier
                .padding(8.dp),
    ) {
        Text(
            text = stringResource(R.string.disclaimer_this_is_a_student_project_with_no_affiliation_with_the_university_of_novi_sad),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Spacing.md),
        )
    }
}

// @Preview(name = "Light Mode")
// @Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
// )
// @Composable
// fun PreviewSettingsScreen() {
//    MenzaNSTheme {
//        val dark = remember { mutableStateOf(false) }
//        SettingsScreen(PaddingValues(20.dp), dark, dark, dark)
//    }
// }
