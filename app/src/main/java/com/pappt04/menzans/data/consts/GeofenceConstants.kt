package com.pappt04.menzans.data.consts

import android.location.Location
import com.google.android.gms.location.Geofence
import com.pappt04.menzans.models.LandmarkDataObject

object GeofenceConstants {
    const val INTENT_ACTION = "GEOFENCE-TRANSITION-INTENT-ACTION"
    const val REQUEST_CODE = 1100

    const val EATING_SPEED_THRESHOLD = 15
    const val DWELL_THRESHOLD = 5

    val LANDMARKS = arrayOf(
        LandmarkDataObject(
            "Menza",
            Location("").apply {
                latitude = 45.245989
                longitude = 19.849117
            },
            25f,
            Geofence.NEVER_EXPIRE
        ),
    )
}
