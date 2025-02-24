package com.pappt04.menzans.data

import android.content.Context
import android.location.Location
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.LocalDate


data class MealData(
    var name: Uitext,
    var price: Int,
    var start_hour: Int,
    var start_minute: Int,
    var end_hour: Int,
    var end_minute: Int
)

sealed class Uitext() {
    data class DynamicString(
        val value: String
    ) : Uitext()

    data class StringResource(
        @StringRes val id: Int
    ) : Uitext()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id)
        }
    }
}

data class NavigationItem(
    val title: Uitext,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val badgeCount: Int? = null,
)

data class LandmarkDataObject(
    val key: String,
    val location: Location,
    val radiusInMeters: Float,
    val expirationTimeInMillis: Long,
)

data class EatingStatisticsData(
    val date: LocalDate,
    val timeentered: String,
    val timeexited: String,
    val tokentype: Uitext
)

data class MealEventString(
    var userid: String,
    var date: String,
    var entered: String,
    var exited: String,
    var token: String
)

data class EnterEventString(
    var userid: String,
    var date: String,
    var enterTime: String,
)

data class ExitEventString(
    var userid: String,
    var exitTime: String,
    var token:String
)

data class UserIDString(
    var userid: String
)

data class  WaitTime(
    var linelength: String,
    var waittime: String,
    var validtime: String,
    var trajectory: String,
    var precision: String,
)