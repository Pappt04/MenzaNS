package com.pappt04.menzans.data.consts

import android.Manifest
import android.os.Build
import com.pappt04.menzans.R
import com.pappt04.menzans.models.AppPermission
import com.pappt04.menzans.models.Uitext

object PermissionData {
    val permissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        )
    }

    val permissionExplanations = listOf(
        AppPermission(Uitext.StringResource(R.string.approximate_location), Uitext.StringResource(R.string.explanation_COARSE_LOCATION)),
        AppPermission(Uitext.StringResource(R.string.precise_location), Uitext.StringResource(R.string.explanation_FINE_LOCATION)),
        AppPermission(Uitext.StringResource(R.string.background_location), Uitext.StringResource(R.string.explanation_BACKGROUND_LOCATION)),
        AppPermission(Uitext.StringResource(R.string.notification), Uitext.StringResource(R.string.explanation_NOTIFICATION_ACCESS)),
    )
}
