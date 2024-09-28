package com.pappt04.menzans

import android.graphics.drawable.Icon
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

object DummyData {
    var MealSample = listOf(
        MealData(Uitext.StringResource(R.string.breakfast), 56, 7, 0, 9, 30),
        MealData(Uitext.StringResource(R.string.lunch), 120, 11, 0, 15, 0),
        MealData(Uitext.StringResource(R.string.dinner), 90, 18, 0, 20, 30)
    )

    var RemainingONCardSample = arrayOf(1, 2, 3, 4)

    val FileNames = listOf("breakfast", "lunch", "dinner", "balance")
    val CardHolderFileName = "cardholderdata"
    val FileGeoFenceEntered="geofenceentered"
    val FileDarkThemeEnabled="darkthemeenabled"


    val CHANNEL_IDs = listOf("Record Meals", "Reminders")
    val NOTIFICATION_IDs = (30..32)

    val ACTION_DISMISS="DISMISS"
    val ACTION_CONFIRM="CONFIRM"
    val ACTION_TWICE="TWICE"
    val ACTION_TOPUP="TOPUP"

    const val CUSTOM_INTENT_GEOFENCE = "GEOFENCE-TRANSITION-INTENT-ACTION"
    const val CUSTOM_REQUEST_CODE_GEOFENCE = 1100

    const val EATING_SPEED_TRESHOLD=15

    data class NavigationItem(
        val title: Uitext,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val route: String,
        val badgeCount: Int? = null,
    )

    val navigationItemData = listOf(
        NavigationItem(
            title = Uitext.StringResource(R.string.card),
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            route = "ScaffoldDesign"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.statistics),
            selectedIcon = Icons.Filled.LocationOn,
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
            title = Uitext.StringResource(R.string.edit),
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit,
            route = "EditScreen"
        ),
        NavigationItem(
            title = Uitext.StringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "SettingsScreen"
        )
    )

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
            15f,
            Geofence.NEVER_EXPIRE
        )
    )

    data class linkContainer(val name: Uitext, val link: String)
    val usefullLinks= listOf(
        linkContainer(Uitext.StringResource(R.string.student_center_novi_sad),"https://www.scns.rs/"),
        linkContainer(Uitext.StringResource(R.string.student_menza),"https://www.scns.rs/sektor-ishrane/"),
        linkContainer(Uitext.StringResource(R.string.zavod_za_zdravstvenu_zastitu_studenata_novi_sad),"https://www.zzzzsns.co.rs"),
        linkContainer(Uitext.StringResource(R.string.new_facebook_group_for_students_in_novi_sad),"https://www.facebook.com/groups/294889734815953"),
        linkContainer(Uitext.StringResource(R.string.center_for_career_and_work),"https://www.infostud.com"),
        linkContainer(Uitext.StringResource(R.string.university_of_novi_sad),"https://www.uns.ac.rs/index.php/"),
        linkContainer(Uitext.StringResource(R.string.university_library),"https://www.uns.ac.rs/index.php/en/faculties/university-centres/central-library"),
        linkContainer(Uitext.StringResource(R.string.moja_kartica_discounts_serbia),"https://mojakartica.rs"),
        linkContainer(Uitext.StringResource(R.string.international_student_identity_card_discounts),"https://www.isic.org"),
    )

    val allUnsAcRswebsites= listOf(
        linkContainer(Uitext.StringResource(R.string.faculty_of_technical_sciences),"http://www.ftn.uns.ac.rs/691618389/fakultet-tehnickih-nauka"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_agriculture),"http://polj.uns.ac.rs"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_economics_in_subotica),"https://www.ef.uns.ac.rs"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_law),"https://pf.uns.ac.rs/rs/"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_philosophy),"https://www.ff.uns.ac.rs"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_technology),"https://www.tf.uns.ac.rs/en#lat"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_medicine),"https://www.mf.uns.ac.rs/En/index_Eng.php"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_sciences),"https://www.pmf.uns.ac.rs/en/"),
        linkContainer(Uitext.StringResource(R.string.academy_of_arts),"https://en.akademija.uns.ac.rs"),
        linkContainer(Uitext.StringResource(R.string.faculty_of_sport_and_physical_education),"https://fspe.edu.rs"),
    )

}