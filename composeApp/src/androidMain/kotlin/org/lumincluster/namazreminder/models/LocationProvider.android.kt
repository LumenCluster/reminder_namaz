package org.lumincluster.namazreminder.models

import android.Manifest


import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import org.lumincluster.namazreminder.appContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


actual suspend fun getUserLocation(): UserLocation {
    val permission = ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION)
    if (permission != PackageManager.PERMISSION_GRANTED) {
        throw SecurityException("Location permission not granted")
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)

    val location: Location = suspendCancellableCoroutine { cont ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(location)
                } else {
                    cont.resumeWithException(NullPointerException("Location is null"))
                }
            }
            .addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    val geocoder = Geocoder(appContext, Locale.getDefault())
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
    val address = if (!addresses.isNullOrEmpty()) addresses[0] else null

    return UserLocation(
        latitude = location.latitude,
        longitude = location.longitude,
        city = address?.locality ?: "Unknown",
        country = address?.countryName ?: "Unknown"
    )
}