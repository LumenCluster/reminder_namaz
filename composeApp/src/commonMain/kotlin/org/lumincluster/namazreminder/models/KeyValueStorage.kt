package org.lumincluster.namazreminder.models

interface KeyValueStorage {
    fun save(key: String, value: String)
    fun gets(key: String): String?
    fun delete(key: String)
}

