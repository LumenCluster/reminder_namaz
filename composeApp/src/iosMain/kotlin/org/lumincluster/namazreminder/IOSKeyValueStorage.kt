package org.lumincluster.namazreminder
import platform.Foundation.NSUserDefaults

import org.lumincluster.namazreminder.models.KeyValueStorage

class IOSKeyValueStorage : KeyValueStorage {
    private val defaults = NSUserDefaults.standardUserDefaults()


    override fun save(key: String, value: String) {
        defaults.setObject(value, key)
    }

    override fun gets(key: String): String? =
        defaults.stringForKey(key)



    override fun delete(key: String) {
        defaults.removeObjectForKey(key)
    }
}