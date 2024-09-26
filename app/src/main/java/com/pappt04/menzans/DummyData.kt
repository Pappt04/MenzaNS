package com.pappt04.menzans

import android.content.Context
import android.location.Location
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.maps.model.LatLng

object DummyData {
    var MealSample = listOf(
        MealData("Breakfast", 67, 7, 0, 9, 30),
        MealData("Lunch", 120, 11, 0, 15, 0),
        MealData("Dinner", 90, 18, 0, 20, 30)
    )

    var RemainingONCardSample = arrayOf(1, 2, 3, 4)

    val FileNames = listOf("breakfast", "lunch", "dinner", "balance")
    val CardHolderFileName = "cardholderdata"
    val FileGeoFenceEntered="geofenceentered"

    data class NavigationItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val route: String,
        val badgeCount: Int? = null,
    )

    val navigationItemData = listOf(
        NavigationItem(
            title = "Card",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = "ScaffoldDesign"
        ),
        NavigationItem(
            title = "Statistics",
            selectedIcon = Icons.Filled.LocationOn,
            unselectedIcon = Icons.Outlined.LocationOn,
            route = "StatisticsScreen"
        ),
        NavigationItem(
            title = "Edit",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            route = "EditScreen"
        ),
        NavigationItem(
            title = "Info",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "InfoScreen"
        ),

        NavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "SettingsScreen"
        )
    )

    public val CHANNEL_IDs = listOf("Record Meals", "Reminders")
    public val NOTIFICATION_IDs = (30..32)

    public const val CUSTOM_INTENT_GEOFENCE = "GEOFENCE-TRANSITION-INTENT-ACTION"
    public const val CUSTOM_REQUEST_CODE_GEOFENCE = 1100

    data class LandmarkDataObject(
        val key: String,
        val location: Location,
        val radiusInMeters: Float,
        val expirationTimeInMillis: Long,
    )

    val LANDMARK_DATA = arrayOf(
        LandmarkDataObject(
            "Menza",
            Location("").apply {
                latitude=45.2460952
                longitude= 19.8493421
            },
            10f,
            Geofence.NEVER_EXPIRE
        )
    )


}