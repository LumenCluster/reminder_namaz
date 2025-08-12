package org.lumincluster.namazreminder.utils
import org.lumincluster.namazreminder.models.DailyPrayerStatus
import org.lumincluster.namazreminder.models.PrayerRecord
import org.lumincluster.namazreminder.models.PrayerStatus

fun createInitialStatus(
    name: String,
    date: String,
    selectedPrayers: List<String>
): DailyPrayerStatus {
    val defaultTimes = mapOf(
        "Fajr" to "4:36 AM",
        "Zuhr" to "12:30 PM",
        "Asr" to "4:35 PM",
        "Maghrib" to "6:45 PM",
        "Isha" to "8:00 PM"
    )

    val prayers = defaultTimes
        .filterKeys { it in selectedPrayers }
        .mapValues {
            PrayerRecord(time = it.value, status = PrayerStatus.NOT_OFFERED)
        }

    return DailyPrayerStatus(name = name, date = date, prayers = prayers)
}
