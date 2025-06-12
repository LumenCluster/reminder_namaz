package org.lumincluster.namazreminder.models

import kotlinx.datetime.LocalDate

//data class CalendarDay(
//    val date: LocalDate,
//    val isCurrentMonth: Boolean,
//    val isToday: Boolean
//
//)
data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
//    val prayerStatus: NamazUserKV? = null // status for that date
    val prayerStatus: DailyPrayerStatus? = null // status for that date

)


