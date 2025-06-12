package org.lumincluster.namazreminder

import org.lumincluster.namazreminder.models.KeyValueStorage

object KeyValueStorageFactory {
    private lateinit var storage: KeyValueStorage

    fun init(storage: KeyValueStorage) {
        this.storage = storage
    }

    fun getInstance(): KeyValueStorage = storage
fun getInstanceOrNull(): KeyValueStorage? = if (::storage.isInitialized) storage else null
}