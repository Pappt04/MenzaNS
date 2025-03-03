package com.pappt04.menzans.data

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import com.google.android.gms.location.Geofence
import com.pappt04.menzans.R
import java.text.SimpleDateFormat

object DummyData {

    val CHANNEL_IDs = listOf("Record Meals", "Reminders")
    val NOTIFICATION_IDs = (30..32)

    val ACTION_DISMISS = "DISMISS"
    val ACTION_CONFIRM = "CONFIRM"
    val ACTION_TWICE = "TWICE"
    val ACTION_TOPUP = "TOPUP"

    const val CUSTOM_INTENT_GEOFENCE = "GEOFENCE-TRANSITION-INTENT-ACTION"
    const val CUSTOM_REQUEST_CODE_GEOFENCE = 1100

    const val AUTOMATIC_EATING_SPEED_TRESHOLD = 15
    const val DWELL_TRESHOLD = 5
    const val MINIMUM_TOKEN_TRESHOLD = 2

    @SuppressLint("SimpleDateFormat")
    val datetypedate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    val datetypeclock: SimpleDateFormat = SimpleDateFormat("HH:mm")
    @SuppressLint("SimpleDateFormat")
    val datetypemonth: SimpleDateFormat = SimpleDateFormat("M")


    /*--------------------------------------------------------------------------------------------*/

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

    val permissionsExplanations = listOf (
        Uitext.StringResource(R.string.explanation_COARSE_LOCATION),
        Uitext.StringResource(R.string.explanation_FINE_LOCATION),
        Uitext.StringResource(R.string.explanation_BACKGROUND_LOCATION),
        Uitext.StringResource(R.string.explanation_NOTIFICATION_ACCESS),
    )

    /*--------------------------------------------------------------------------------------------*/
    val navDrawerItemData = listOf(
        NavigationItem(
            title = Uitext.StringResource(R.string.dashboard),
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = "DashboardScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.statistics),
            selectedIcon = Icons.Default.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            route = "StatisticsScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.info),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "InfoScreen"
        ),

        NavigationItem(
            title = Uitext.StringResource(R.string.card),
            selectedIcon = Icons.Filled.CreditCard,
            unselectedIcon = Icons.Outlined.CreditCard,
            route = "CardScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "SettingsScreen"
        )
    )

    /*--------------------------------------------------------------------------------------------*/
    val bottomNavItemData = listOf(
        NavigationItem(
            title = Uitext.StringResource(R.string.dashboard),
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = "DashboardScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.statistics),
            selectedIcon = Icons.Default.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            route = "StatisticsScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.info),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "InfoScreen"
        ),

        NavigationItem(
            title = Uitext.StringResource(R.string.card),
            selectedIcon = Icons.Filled.CreditCard,
            unselectedIcon = Icons.Outlined.CreditCard,
            route = "CardScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "SettingsScreen"
        )
    )

    /*--------------------------------------------------------------------------------------------*/
    val dataweek = listOf(
        Uitext.StringResource(R.string.monday),
        Uitext.StringResource(R.string.tuesday),
        Uitext.StringResource(R.string.wednesday),
        Uitext.StringResource(R.string.thursday),
        Uitext.StringResource(R.string.friday),
        Uitext.StringResource(R.string.saturday),
        Uitext.StringResource(R.string.sunday)
    )

    /*--------------------------------------------------------------------------------------------*/
    val LANDMARK_DATA = arrayOf(
        LandmarkDataObject(
            "Menza",
            Location("").apply {
                latitude = 45.245989
                longitude = 19.849117
            },
            50f,
            Geofence.NEVER_EXPIRE
        ),
    )



    /*--------------------------------------------------------------------------------------------*/
    val engmonths = listOf(
        "january",
        "february",
        "march",
        "april",
        "may",
        "june",
        "july",
        "august",
        "september",
        "october",
        "november",
        "december"
    )
    val engmeals = listOf(
        "Breakfast",
        "Lunch",
        "Dinner"
    )

    fun engtosresc(s: String): Int {
        when (s) {
            engmeals[0] -> return R.string.breakfast
            engmeals[1] -> return R.string.lunch
            engmeals[2] -> return R.string.dinner
        }
        return R.string.info
    }

    const val BASE_SERVER_URL="https://apollo4.duckdns.org/"
    const val BASE_API_NAME="/menzaapi"

    val FINANCING_KEY = "financingPreferenceKey"
    val DARK_THEME_KEY = "darkthemePreferenceKey"
    val MATERIALYOU_THEME_KEY = "materialyouPreferenceKey"

    val BREAKFAST_KEY= "breakfastKey"
    val LUNCH_KEY= "lunchKey"
    val DINNER_KEY= "dinnerKey"

}