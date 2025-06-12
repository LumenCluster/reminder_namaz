@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
package org.lumincluster.namazreminder.models

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.*
import platform.Foundation.*
import platform.darwin.NSObject
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getUserLocation(): UserLocation = suspendCancellableCoroutine { cont ->

    val locationManager = CLLocationManager()
    locationManager.requestWhenInUseAuthorization()

    val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val location = didUpdateLocations.lastOrNull() as? CLLocation
            if (location != null) {
                val coordinate = location.coordinate
                val latitude: Double
                val longitude: Double
                coordinate.useContents {
                    latitude = this.latitude
                    longitude = this.longitude
                }

                val geocoder = CLGeocoder()
                geocoder.reverseGeocodeLocation(location) { placemarks, error ->
                    val placemark = placemarks?.firstOrNull() as? CLPlacemark
                    val city = placemark?.locality ?: "Unknown"
                    val country = placemark?.country ?: "Unknown"

                    cont.resume(
                        UserLocation(
                            latitude = latitude,
                            longitude = longitude,
                            city = city,
                            country = country
                        )
                    )
                }
            } else {
                cont.resume(UserLocation(0.0, 0.0, "Unknown", "Unknown"))
            }
        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            cont.resume(UserLocation(0.0, 0.0, "Unknown", "Unknown"))
        }
    }

    locationManager.delegate = delegate
    locationManager.startUpdatingLocation()
}
