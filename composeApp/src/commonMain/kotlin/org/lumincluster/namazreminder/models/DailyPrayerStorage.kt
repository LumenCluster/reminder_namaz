package org.lumincluster.namazreminder.storage

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lumincluster.namazreminder.models.DailyPrayerStatus
import org.lumincluster.namazreminder.models.KeyValueStorage
import kotlin.text.get
import kotlin.text.set

object DailyPrayerStorage {
    private const val STORAGE_KEY = "DAILY_PRAYER_DATA"
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

    fun save(storage: KeyValueStorage, status: DailyPrayerStatus) {
        val all = getAll(storage).toMutableList()
        val existingIndex = all.indexOfFirst { it.date == status.date }

        if (existingIndex != -1) {
            all[existingIndex] = status
        } else {
            all.add(status)
        }

        val encoded = json.encodeToString(all)
        storage.save(STORAGE_KEY, encoded)
    }

//    fun getAll(storage: KeyValueStorage): List<DailyPrayerStatus> {
//        val data = storage.gets(STORAGE_KEY) ?: return emptyList()
//        return runCatching { json.decodeFromString(data) }.getOrElse { emptyList() }
//    }
fun getAll(storage: KeyValueStorage): List<DailyPrayerStatus> {
    val data = storage.gets(STORAGE_KEY) ?: return emptyList()
    return runCatching {
        json.decodeFromString<List<DailyPrayerStatus>>(data)
    }.getOrElse { emptyList() }
}


    fun getByDate(storage: KeyValueStorage, date: String): DailyPrayerStatus? {
        return getAll(storage).find { it.date == date }
    }

    fun updatePrayerStatus(
        storage: KeyValueStorage,
        date: String,
        prayerName: String,
        newStatus: String
    ) {
        val all = getAll(storage).toMutableList()
        val index = all.indexOfFirst { it.date == date }

        if (index != -1) {
            val record = all[index]
            val updatedPrayers = record.prayers.toMutableMap()
            val existing = updatedPrayers[prayerName] ?: return

            updatedPrayers[prayerName] = existing.copy(
                status = newStatus.uppercase().let {
                    org.lumincluster.namazreminder.models.PrayerStatus.valueOf(it)
                }
            )

            val updatedRecord = record.copy(prayers = updatedPrayers)
            all[index] = updatedRecord
            val encoded = json.encodeToString(all)
            storage.save(STORAGE_KEY, encoded)
        }
    }

    fun clear(storage: KeyValueStorage) {
        storage.save(STORAGE_KEY, "")
    }
}