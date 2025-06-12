package org.lumincluster.namazreminder.utils

import com.kizitonwose.calendar.core.YearMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.lumincluster.namazreminder.models.PrayerTimes

class DateUtils {
    fun YearMonth.atDay(day: Int): LocalDate = LocalDate(this.year, this.month, day)
}



