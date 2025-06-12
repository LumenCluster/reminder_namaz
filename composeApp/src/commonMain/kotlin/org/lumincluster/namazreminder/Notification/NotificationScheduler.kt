package org.lumincluster.namazreminder.Notification

import org.lumincluster.namazreminder.models.NamazTime
import org.lumincluster.namazreminder.models.PrayerTimes
import kotlin.native.ObjCName
interface NotificationScheduler {
    fun scheduleNamazNotification(namazTime: NamazTime)
    fun cancelAllNotifications()
}