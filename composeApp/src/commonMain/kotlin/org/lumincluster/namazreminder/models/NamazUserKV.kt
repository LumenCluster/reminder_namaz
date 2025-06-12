package org.lumincluster.namazreminder.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NamazUserKV(
    val id: Long,
    val date: String,
    val fajr: String,
    val zuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)
object NamazUserStorage {
    private const val NAMAZ_KEY = "NAMAZ_USERS"

    fun saveUsers(storage: KeyValueStorage, users: List<NamazUserKV>) {
        val json = Json.encodeToString(users)
        storage.save(NAMAZ_KEY, json)
    }

    fun getUsers(storage: KeyValueStorage): List<NamazUserKV> {
        val json = storage.gets(NAMAZ_KEY) ?: return emptyList()
        return runCatching { Json.decodeFromString<List<NamazUserKV>>(json) }.getOrDefault(emptyList())
    }

    fun addUser(storage: KeyValueStorage, user: NamazUserKV) {
        val users = getUsers(storage).toMutableList()
        val newId = (users.maxOfOrNull { it.id } ?: 0L) + 1
        users.add(user.copy(id = newId))
        saveUsers(storage, users)
    }

    private val json = Json { ignoreUnknownKeys = true }

    fun updateNamazUser(storage: KeyValueStorage, id: Long, date: String, updates: NamazUserKV.() -> Unit) {
        val key = "user_$id"

        val existingJson = storage.gets(key) ?: return
        val user = json.decodeFromString<NamazUserKV>(existingJson)

        if (user.date == date) {
            updates(user) // Apply field updates using lambda
            val updatedJson = json.encodeToString(NamazUserKV.serializer(), user)
            storage.save(key, updatedJson)
        }
    }
}