package org.lumincluster.namazreminder

import android.content.Context
import android.content.SharedPreferences
import org.lumincluster.namazreminder.models.KeyValueStorage

class AndroidKeyValueStorage(context: Context) : KeyValueStorage {
    private val prefs: SharedPreferences = context.getSharedPreferences("NamazPrefs", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    override fun gets(key: String): String? = prefs.getString(key, null)


    override fun delete(key: String) {
        prefs.edit().remove(key).apply()
    }
}