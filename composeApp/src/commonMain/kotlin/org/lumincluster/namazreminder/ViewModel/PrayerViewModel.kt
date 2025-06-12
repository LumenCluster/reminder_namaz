package org.lumincluster.namazreminder.ViewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.lumincluster.namazreminder.data.PrayerApi
import org.lumincluster.namazreminder.models.PrayerTimes

class PrayerViewModel() {
    private val api = PrayerApi()
    private val _state = MutableStateFlow<PrayerTimes?>(null)
    val state: StateFlow<PrayerTimes?> = _state

    private val scope = CoroutineScope(Dispatchers.Default)

    // Fetch prayer times using location (latitude & longitude)
    fun fetchPrayerTimes(latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.Default).launch {

                _state.value = api.getPrayerTimes(latitude,longitude)

        }
    }
    suspend fun fetchPrayerTimesBlocking(latitude: Double, longitude: Double): PrayerTimes? {
        return api.getPrayerTimes(latitude, longitude)
    }
    fun getUpcomingPrayer(prayerTimes: PrayerTimes): Pair<String, LocalTime>? {
        val currentTime = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).time

        println("Current time: $currentTime")

        val times = listOfNotNull(
            prayerTimes.fajr.toLocalTimeOrNull()?.let { "Fajr" to it },
            prayerTimes.dhuhr.toLocalTimeOrNull()?.let { "Dhuhr" to it },
            prayerTimes.asr.toLocalTimeOrNull()?.let { "Asr" to it },
            prayerTimes.maghrib.toLocalTimeOrNull()?.let { "Maghrib" to it },
            prayerTimes.isha.toLocalTimeOrNull()?.let { "Isha" to it },
        )

        println("Parsed prayer times: $times")

        val upcoming = times.firstOrNull { it.second > currentTime } ?: times.firstOrNull()

        println("Upcoming: $upcoming")
        return upcoming
    }

    fun clear() {
        scope.cancel()
    }
}

fun String.toLocalTimeOrNull(): LocalTime? {
    return try {
        val parts = this.trim().split(":")
        if (parts.size >= 2) {
            val hour = parts[0].toIntOrNull() ?: return null
            val minute = parts[1].toIntOrNull() ?: return null
            LocalTime(hour, minute)
        } else null
    } catch (e: Exception) {
        null
    }
}