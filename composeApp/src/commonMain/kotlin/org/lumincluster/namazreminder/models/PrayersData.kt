package org.lumincluster.namazreminder.models
import kotlinx.serialization.Serializable

class PrayersData {


}
@Serializable
enum class PrayerStatus {
    NOT_OFFERED,
    ON_TIME,
    QAZA
}

@Serializable
data class PrayerRecord(
    val time: String, // e.g., "4:36 AM"
    val status: PrayerStatus // enum: NOT_OFFERED, ON_TIME, QAZA
)


@Serializable
data class DailyPrayerStatus(
    val name: String, // user's name
    val date: String, // e.g., "2025-07-16"
    val prayers: Map<String, PrayerRecord> // key = "Fajr", "Zuhr" etc.
)
