package org.lumincluster.namazreminder.UniqueUserId

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID
import kotlin.random.Random

class UserIdManagerImpl(private val context: Context) : UserIdManager {


    companion object {
        private const val PREFS_NAME = "app_prefs"
        private const val USER_ID_KEY = "unique_user_id"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun getUserId(): Int {
        val existingId = prefs.getInt(USER_ID_KEY, -1)
        return if (existingId != -1) {
            existingId
        } else {
            val newId = Random.nextInt(100_000, 999_999_999)
            prefs.edit().putInt(USER_ID_KEY, newId).apply()
            newId
        }
    }
}