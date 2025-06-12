package org.lumincluster.namazreminder.Notification
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationOptions
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNNotificationSettings
import platform.UserNotifications.UNNotificationCategory
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

class IOSPermissionHelper : PermissionHelper {

    override fun requestNotificationPermission(callback: (Boolean) -> Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.requestAuthorizationWithOptions(
            options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge,
            completionHandler = { granted, _ ->
                println("iOS: Permission granted = $granted")
                callback(granted)
            }
        )
    }

    override suspend fun hasNotificationPermission(): Boolean {
        return suspendCancellableCoroutine { cont ->
            UNUserNotificationCenter.currentNotificationCenter()
                .getNotificationSettingsWithCompletionHandler { settings ->
                    val granted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized ||
                            settings?.authorizationStatus == UNAuthorizationStatusProvisional
                    cont.resume(granted)
                }
        }
    }
}