package org.lumincluster.namazreminder.Notification

import org.lumincluster.namazreminder.models.NamazTime
import platform.Foundation.NSDateComponents
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter

class IosNamazNotificationScheduler: NotificationScheduler {

    override fun scheduleNamazNotification(namazTime: NamazTime) {
        val content = UNMutableNotificationContent().apply {
            setTitle("Time for ${namazTime.name}")
            setBody("It's time for ${namazTime.name} prayer.")
            setSound(UNNotificationSound.defaultSound())

        }

        val dateComponents = NSDateComponents().apply {
            hour = namazTime.hour.toLong()
            minute = namazTime.minute.toLong()
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents, repeats = true
        )

        val request = UNNotificationRequest.requestWithIdentifier(
            namazTime.name,
            content,
            trigger
        )

        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(
            request,
            withCompletionHandler = null
        )
    }

    override fun cancelAllNotifications() {
        UNUserNotificationCenter.currentNotificationCenter().removeAllPendingNotificationRequests()
    }
}