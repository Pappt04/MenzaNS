package com.pappt04.menzans.models

import android.location.Location

data class LandmarkDataObject(
    val key: String,
    val location: Location,
    val radiusInMeters: Float,
    val expirationTimeInMillis: Long,
)
