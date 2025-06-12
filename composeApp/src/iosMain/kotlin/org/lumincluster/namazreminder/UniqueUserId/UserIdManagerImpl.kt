package org.lumincluster.namazreminder.UniqueUserId
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUUID
import kotlin.random.Random

class UserIdManagerImpl : UserIdManager {

    companion object {
        private const val USER_ID_KEY = "unique_user_id"
    }

    private val defaults = NSUserDefaults.standardUserDefaults

    override fun getUserId(): Int {
        val existingId = defaults.integerForKey(USER_ID_KEY)
        if (existingId.toInt() != 0) return existingId.toInt()

        val newId = Random.nextInt(100_000, 999_999_999)
        defaults.setInteger(newId.toLong(), forKey = USER_ID_KEY)
        return newId
    }
}