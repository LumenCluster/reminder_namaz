package org.lumincluster.namazreminder.utils

import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.lumincluster.namazreminder.models.PrayerTimes

//class DateUtils {
//    fun YearMonth.atDay(day: Int): LocalDate = LocalDate(this.year, this.month, day)
//}


object DateUtils {
    fun YearMonth.atDay(day: Int): LocalDate = LocalDate(this.year, this.month, day)

    fun getCurrentDate(): String {
        val now = Clock.System.now()
        val local = now.toLocalDateTime(TimeZone.currentSystemDefault())
        return local.date.toString() // formatted string
    }

    fun getCurrentLocalDate(): LocalDate {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}
