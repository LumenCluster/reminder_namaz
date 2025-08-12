package org.lumincluster.namazreminder.models

object UserPrefStorage {
    private const val IS_FIRST_TIME_KEY = "IS_FIRST_TIME_USER"
    private const val USER_NAME_KEY = "USER_NAME"

    fun markFirstTimeDone(storage: KeyValueStorage) {
        storage.save(IS_FIRST_TIME_KEY, "false")
    }

    // Fix: Handle a nullable storage instance
    fun isFirstTime(storage: KeyValueStorage?): Boolean {
        return storage?.gets(IS_FIRST_TIME_KEY) != "false"
    }

    fun saveUserName(storage: KeyValueStorage, name: String) {
        storage.save(USER_NAME_KEY, name)
    }

    fun getUserName(storage: KeyValueStorage): String? {
        return storage.gets(USER_NAME_KEY)
    }
}

