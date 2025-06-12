package org.lumincluster.namazreminder.Notification

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.assarpic
import namazreminder.composeapp.generated.resources.fajarpic
import namazreminder.composeapp.generated.resources.ishapic
import namazreminder.composeapp.generated.resources.maghribpic
import namazreminder.composeapp.generated.resources.zuharpic
import org.lumincluster.namazreminder.Screens.formatTo12HourKMP
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.ViewModel.toLocalTimeOrNull
import org.lumincluster.namazreminder.models.NamazTime
import org.lumincluster.namazreminder.models.Namazlist

class NamazViewModel(
    private val scheduler: NotificationScheduler
) {
    fun scheduleDailyNamazNotifications() {
        val geoLocation = Geolocator.mobile()
        val prayerViewModel = PrayerViewModel()

        CoroutineScope(Dispatchers.Default).launch {
            when (val result = geoLocation.current()) {
                is GeolocatorResult.Success -> {
                    val coords = result.data.coordinates
                    val prayerTimes = prayerViewModel.fetchPrayerTimesBlocking(coords.latitude, coords.longitude)
                    scheduleAll(prayerTimes!!)
                }
                is GeolocatorResult.Error -> {
                    // fallback to Makkah
                    val prayerTimes = prayerViewModel.fetchPrayerTimesBlocking(21.3891, 39.8579)
                    scheduleAll(prayerTimes!!)
                }
            }
        }
    }

    private fun scheduleAll(prayerTimes: org.lumincluster.namazreminder.models.PrayerTimes) {
        val times = listOfNotNull(
            prayerTimes.fajr.toLocalTimeOrNamazTime("Fajr"),
            prayerTimes.dhuhr.toLocalTimeOrNamazTime("Dhuhr"),
            prayerTimes.asr.toLocalTimeOrNamazTime("Asr"),
            prayerTimes.maghrib.toLocalTimeOrNamazTime("Maghrib"),
            prayerTimes.isha.toLocalTimeOrNamazTime("Isha"),
        )
        times.forEach { scheduler.scheduleNamazNotification(it) }
    }

    private fun String.toLocalTimeOrNamazTime(name: String): NamazTime? {
        val localTime: LocalTime = this.toLocalTimeOrNull() ?: return null
        return NamazTime(name, localTime.hour, localTime.minute)
    }



}

