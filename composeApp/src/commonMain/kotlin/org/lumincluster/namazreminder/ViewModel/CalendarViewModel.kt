package org.lumincluster.namazreminder.ViewModel

import androidx.compose.ui.input.key.Key.Companion.Calendar
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.atDay
import com.kizitonwose.calendar.core.atEndOfMonth

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate

import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.lumincluster.namazreminder.KeyValueStorageFactory
import org.lumincluster.namazreminder.models.CalendarDay
import org.lumincluster.namazreminder.models.NamazUserKV
import org.lumincluster.namazreminder.models.NamazUserStorage

class CalendarViewModel {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val yesterday = today.minus(1, DateTimeUnit.DAY)

    val currentYear = today.year

    val calendarDays: List<List<CalendarDay>> = (1..12).map { month ->
        val yearMonth = YearMonth(currentYear, month)
        val daysInMonth = yearMonth.atEndOfMonth().dayOfMonth
        val firstDay = yearMonth.atDay(1)

        (0 until daysInMonth).map { offset ->
            val date = firstDay.plus(offset, DateTimeUnit.DAY)
            CalendarDay(
                date = date,
                isCurrentMonth = date.monthNumber == month,
                isToday = date == today,
                prayerStatus = getPrayerStatusForDate(date) // 👈 load status
            )
        }
    }

    fun getPrayerStatusForDate(date: LocalDate): NamazUserKV? {
        val storage = KeyValueStorageFactory.getInstanceOrNull() ?: return null
        val users = NamazUserStorage.getUsers(storage)
        return users.find { it.date == date.toString() }
    }

    fun getYesterday(): LocalDate = yesterday
}
fun getYesterday(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(DatePeriod(days = 1))
}

//fun getPrayerStatusForDate(date: LocalDate): NamazStatus? {
//    return _namazStatusMap[date] // your saved data map
//}



   /* private val _currentMonth = MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val currentMonth: StateFlow<LocalDate> = _currentMonth

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate

    val monthTitle = mutableStateOf("")

    init {
        updateMonthTitle(_currentMonth.value)
    }

    fun onMonthChanged(newMonth: LocalDate) {
        coroutineScope.launch(Dispatchers.Main) {
            _currentMonth.value = newMonth
            updateMonthTitle(newMonth)
        }
    }

    fun onDateSelected(date: LocalDate) {
        coroutineScope.launch(Dispatchers.Main) {
            _selectedDate.value = date
        }
    }

    private fun updateMonthTitle(date: LocalDate) {
        monthTitle.value = "${date.month.name} ${date.year}"
    }

    fun getDaysOfWeek(): List<DayOfWeek> {
        return DayOfWeek.values().toList()
    }

    fun generateDates(month: LocalDate): List<LocalDate> {
        val firstDayOfMonth = LocalDate(month.year, month.month, 1)
        val firstDayOfWeekIndex = firstDayOfMonth.dayOfWeek.isoDayNumber % 7 // Sunday = 0

        val firstDayNextMonth = if (month.monthNumber == 12)
            LocalDate(month.year + 1, 1, 1)
        else
            LocalDate(month.year, month.monthNumber + 1, 1)

        val daysInMonth = (firstDayNextMonth - firstDayOfMonth).days

        val previousMonthDays = (0 until firstDayOfWeekIndex).map {
            firstDayOfMonth.minus((firstDayOfWeekIndex - it).toLong(), DateTimeUnit.DAY)
        }

        val currentMonthDays = (0 until daysInMonth).map {
            firstDayOfMonth.plus(it.toLong(), DateTimeUnit.DAY)
        }

        val totalDays = previousMonthDays + currentMonthDays
        val daysNeeded = 42 - totalDays.size

        val nextMonthDays = (1..daysNeeded).map {
            currentMonthDays.last().plus(it.toLong(), DateTimeUnit.DAY)
        }

        return previousMonthDays + currentMonthDays + nextMonthDays
    }*/
//   class CalendarViewModel() {
//
//    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
//    val currentYear = today.year
//    val calendarDays: List<List<CalendarDay>> = (1..12).map { month ->
//        val yearMonth = YearMonth(currentYear, month)
//        val daysInMonth = yearMonth.atEndOfMonth().dayOfMonth
//        val firstDay = yearMonth.atDay(1)
////        val lastDay = yearMonth.atDay(daysInMonth)
//
//        (0 until daysInMonth).map { offset ->
//            val date = firstDay.plus(offset, DateTimeUnit.DAY)
//            CalendarDay(date, date.monthNumber == month, date == today)
//        }
//
//
//    }
//}