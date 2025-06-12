package org.lumincluster.namazreminder.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.atDay
import com.kizitonwose.calendar.core.atEndOfMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.lumincluster.namazreminder.KeyValueStorageFactory
import org.lumincluster.namazreminder.models.CalendarDay
import org.lumincluster.namazreminder.models.DailyPrayerStatus
import org.lumincluster.namazreminder.models.KeyValueStorage
import org.lumincluster.namazreminder.models.NamazUserStorage
import org.lumincluster.namazreminder.models.PrayerRecord
import org.lumincluster.namazreminder.models.PrayerStatus


class CalendarViewModel {

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    private val yesterday = today.minus(1, DateTimeUnit.DAY)
    private val storage: KeyValueStorage? get() = KeyValueStorageFactory.getInstanceOrNull()

    val currentYear = today.year

    // NEW: Holds selected day's status
    private val _selectedDayStatus = mutableStateOf<DailyPrayerStatus?>(null)
    val selectedDayStatus: State<DailyPrayerStatus?> = _selectedDayStatus

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
                prayerStatus = getPrayerStatusForDate(date)
            )
        }
    }

    fun getPrayerStatusForDate(date: LocalDate): DailyPrayerStatus? {
        val storage = storage ?: return null
        val data = NamazUserStorage.getUserByDate(storage, date.toString())
        _selectedDayStatus.value = data // <-- important: trigger recomposition
        return data
    }

    fun setSelectedDate(date: LocalDate) {
        getPrayerStatusForDate(date)
    }

    fun updatePrayerStatus(date: String, prayerName: String, status: PrayerStatus) {
        val storage = storage ?: return
        val storageKey = when (prayerName.uppercase()) {
            "FAJAR" -> "Fajr"
            "ZUHR" -> "Zuhr"
            "ASR" -> "Asr"
            "MAGHRIB" -> "Maghrib"
            "ISHA" -> "Isha"
            else -> prayerName
        }

        NamazUserStorage.updateUserByDate(storage, date) { current ->
            val updatedPrayers = current.prayers.toMutableMap()
            updatedPrayers[storageKey] = PrayerRecord(
                time = updatedPrayers[storageKey]?.time ?: "",
                status = status
            )
            current.copy(prayers = updatedPrayers)
        }

        // Refresh after update
        getPrayerStatusForDate(LocalDate.parse(date))
    }

    fun getYesterday(): LocalDate = yesterday
}


