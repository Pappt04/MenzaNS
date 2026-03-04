package com.pappt04.menzans.geolocation

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.pappt04.menzans.data.consts.GeofenceConstants

/**
 * Re-registers geofences after device reboot.
 * Android clears all geofences on reboot, so without this receiver
 * automatic meal detection silently stops working until the app is opened.
 */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // Only register if the user has already granted location permission
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val geofenceManager = GeofenceManager(context)
        for (geofence in GeofenceConstants.LANDMARKS) {
            geofenceManager.addGeofence(
                geofence.key,
                geofence.location,
                geofence.radiusInMeters,
                geofence.expirationTimeInMillis,
            )
        }
        geofenceManager.registerGeofence()
    }
}
