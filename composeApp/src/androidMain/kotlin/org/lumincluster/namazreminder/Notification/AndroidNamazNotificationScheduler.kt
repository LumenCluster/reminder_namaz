package org.lumincluster.namazreminder.Notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Build
import android.provider.Settings
import org.lumincluster.namazreminder.models.NamazTime
import java.util.Calendar

class AndroidNamazNotificationScheduler ( private val context: Context
) : NotificationScheduler {

    override fun scheduleNamazNotification(namazTime: NamazTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
            putExtra("NAMAZ_NAME", namazTime.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            namazTime.name.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE  // ✅ FIXED
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, namazTime.hour)
            set(Calendar.MINUTE, namazTime.minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Check if the app can schedule exact alarms
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    // Prompt user to allow exact alarms
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
            } else {
                // Pre-Android 12: no permission needed
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            // Optional: handle fallback or notify user
        }
    }

    override fun cancelAllNotifications() {
        // Clear all alarms if needed
    }
}