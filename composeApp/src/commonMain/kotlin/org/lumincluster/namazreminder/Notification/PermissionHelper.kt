package org.lumincluster.namazreminder.Notification

interface PermissionHelper {
    fun requestNotificationPermission(callback: (Boolean) -> Unit)
    suspend fun hasNotificationPermission(): Boolean
}