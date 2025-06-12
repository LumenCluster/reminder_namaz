package org.lumincluster.namazreminder.Notification

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionHandler(private val permissionHelper: PermissionHelper) {
    suspend fun checkAndRequestPermission(): Boolean {
        return if (permissionHelper.hasNotificationPermission()) {
            true
        } else {
            suspendCancellableCoroutine { cont ->
                permissionHelper.requestNotificationPermission { granted ->
                    cont.resume(granted)
                }
            }
        }
    }
}