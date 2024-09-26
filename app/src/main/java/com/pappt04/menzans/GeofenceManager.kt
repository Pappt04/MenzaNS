package com.pappt04.menzans

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_DWELL
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.pappt04.menzans.DummyData.CUSTOM_INTENT_GEOFENCE
import com.pappt04.menzans.DummyData.CUSTOM_REQUEST_CODE_GEOFENCE
import kotlinx.coroutines.tasks.await

class GeofenceManager(context: Context) {

    private val TAG = "GeofenceManager"
    private val client = LocationServices.getGeofencingClient(context)
    val geofenceList = mutableMapOf<String, Geofence>()

    private val geofencingPendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            CUSTOM_REQUEST_CODE_GEOFENCE,
            intent,
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                PendingIntent.FLAG_CANCEL_CURRENT
            } else {
                PendingIntent.FLAG_MUTABLE
            }
        )
    }

    fun addGeofence(
        key: String,
        location: Location,
        radiusInMeters: Float = 10.0f,
        expirationTimeInMillis: Long = 30 * 60 * 1000,
    ) {
        geofenceList[key] = createGeofence(key, location, radiusInMeters, expirationTimeInMillis)
    }

    fun removeGeofence(key: String) {
        geofenceList.remove(key)
    }

    @SuppressLint("MissingPermission")
    fun registerGeofence() {
        client.addGeofences(createGeofencingRequest(), geofencingPendingIntent)?.run {
            addOnSuccessListener {
                Log.d(TAG, "registerGeofence: SUCCESS")
            }
            addOnFailureListener { exception ->
                Log.d(TAG, "registerGeofence: Failure\n$exception")
            }
        }
    }

    suspend fun deregisterGeofence() = kotlin.runCatching {
        client.removeGeofences(geofencingPendingIntent).await()
        geofenceList.clear()
    }

    private fun createGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GEOFENCE_TRANSITION_ENTER)
            addGeofences(geofenceList.values.toList())
        }.build()
    }

    private fun createGeofence(
        key: String,
        location: Location,
        radiusInMeters: Float,
        expirationTimeInMillis: Long,
    ): Geofence {
        return Geofence.Builder()
            .setRequestId(key)
            .setCircularRegion(location.latitude, location.longitude, radiusInMeters)
            .setExpirationDuration(expirationTimeInMillis)
            .setTransitionTypes(GEOFENCE_TRANSITION_ENTER or GEOFENCE_TRANSITION_EXIT)
            .build()
    }

}