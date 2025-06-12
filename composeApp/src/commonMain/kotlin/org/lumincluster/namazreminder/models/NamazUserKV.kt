package org.lumincluster.namazreminder.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

//@Serializable
//data class NamazUserKV(
//    val id: Long,
//    val name: String,           // ✅ name of the user (added)
//    val date: String,           // e.g. "2025-07-15"
//    val fajr: String,
//    val zuhr: String,
//    val asr: String,
//    val maghrib: String,
//    val isha: String
//)
//@Serializable
//data class NamazUserKV(
//    val id: Long = 0L,
//    val name: String,
//    val date: String,
//    val fajr: Boolean,
//    val zuhr: Boolean,
//    val asr: Boolean,
//    val maghrib: Boolean,
//    val isha: Boolean
//)


import kotlinx.serialization.json.Json

object NamazUserStorage {
    private const val NAMAZ_KEY = "NAMAZ_USERS"
    private val json = Json { ignoreUnknownKeys = true }

    fun saveUsers(storage: KeyValueStorage, users: MutableList<DailyPrayerStatus>) {
        val jsonData = json.encodeToString(users)
        storage.save(NAMAZ_KEY, jsonData)
    }

    fun getUsers(storage: KeyValueStorage?): List<DailyPrayerStatus> {
        if (storage == null) return emptyList()
        val jsonData = storage.gets(NAMAZ_KEY) ?: return emptyList()
        return runCatching {
            json.decodeFromString<List<DailyPrayerStatus>>(jsonData)
        }.getOrDefault(emptyList())
    }

    fun addUser(storage: KeyValueStorage, user: DailyPrayerStatus) {
        val users = getUsers(storage).toMutableList()
        users.add(user)
        saveUsers(storage, users)
    }

    fun updateUserByDate(
        storage: KeyValueStorage,
        date: String,
        updates: (DailyPrayerStatus) -> DailyPrayerStatus
    ) {
        val users = getUsers(storage).toMutableList()
        val index = users.indexOfFirst { it.date == date }

        if (index != -1) {
            users[index] = updates(users[index])
        } else {
            val newUser = updates(DailyPrayerStatus(
                name = "",
                date = date,
                prayers = emptyMap()
            ))
            users.add(newUser)
        }
        saveUsers(storage, users)
    }

    fun getUserByDate(storage: KeyValueStorage, date: String): DailyPrayerStatus? {
        return getUsers(storage).find { it.date == date }
    }
}

