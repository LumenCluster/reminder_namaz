package org.lumincluster.namazreminder.Screens
//import androidx.compose.material.Icon
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MoreVert // For Icons.Default.MoreVert

// This composable is part of the screen's functionality

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.datetime.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.arrowback
import namazreminder.composeapp.generated.resources.assarpic
import namazreminder.composeapp.generated.resources.calenderback
import namazreminder.composeapp.generated.resources.calendernext
import namazreminder.composeapp.generated.resources.fajarpic
import namazreminder.composeapp.generated.resources.ishapic
import namazreminder.composeapp.generated.resources.maghribpic
import namazreminder.composeapp.generated.resources.zuharpic
import org.jetbrains.compose.resources.painterResource
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.models.Namazlist
import org.lumincluster.namazreminder.models.PrayerRecord
import org.lumincluster.namazreminder.models.PrayerStatus
import org.theme.boxcolor
import org.theme.greencolor


@Composable
fun PrayerStatusToggle(
    currentStatus: PrayerStatus,
    onStatusChanged: (PrayerStatus) -> Unit,
    enabled: Boolean //  Add this parameter
) {
    val backgroundColor = when (currentStatus) {
        PrayerStatus.NOT_OFFERED -> Color.LightGray
        PrayerStatus.ON_TIME -> Color(0xFF4CAF50) // Green
        PrayerStatus.QAZA -> Color(0xFFFFC107) // Amber
    }

    val statusText = when (currentStatus) {
        PrayerStatus.NOT_OFFERED -> "Not Offered"
        PrayerStatus.ON_TIME -> "On Time"
        PrayerStatus.QAZA -> "Qaza"
    }

    Button(
        onClick = {
            val nextStatus = when (currentStatus) {
                PrayerStatus.NOT_OFFERED -> PrayerStatus.ON_TIME
                PrayerStatus.ON_TIME -> PrayerStatus.QAZA
                PrayerStatus.QAZA -> PrayerStatus.NOT_OFFERED
            }
            onStatusChanged(nextStatus)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = Color.Black,
            disabledBackgroundColor = Color.Gray, //  Optional: make disabled state gray
            disabledContentColor = Color.DarkGray  //  Optional
        ),
        enabled = enabled, //  Apply the passed enabled value here
        modifier = Modifier
            .width(120.dp)
            .height(40.dp)
    ) {
        Text(statusText, fontSize = 14.sp)
    }
}

@Composable
fun MainCalenderScreen(onNext: () -> Unit) {

    val viewModel = remember { CalendarViewModel() }
    val viewModelprayer = remember { PrayerViewModel() }
    val calendarData = viewModel.calendarDays
    //testing
    val _prayerStatuses = mutableStateMapOf<String, PrayerRecord>()
//    val prayerStatuses: Map<String, PrayerRecord> get() = _prayerStatuses

    fun saveInitialPrayerStatus(data: Map<String, PrayerRecord>) {
        _prayerStatuses.putAll(data)
    }

    fun getPrayerStatus(prayer: String): PrayerRecord? = _prayerStatuses[prayer]

    val initialMonthIndex = viewModel.calendarDays.indexOfFirst { month ->
        month.any { it.isToday }
    }
    var currentMonthIndex by remember { mutableStateOf(initialMonthIndex.coerceIn(0, calendarData.lastIndex)) }

//    var selectedDate by remember {
//        mutableStateOf(
//            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
//        )
//    }
    var selectedDate by remember {
        mutableStateOf(
            Clock.System.todayIn(TimeZone.currentSystemDefault()).minus(1, DateTimeUnit.DAY)
        )
    }

    val pagerState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonthIndex)
    val state by viewModelprayer.state.collectAsState()
    val geoLocation = remember { Geolocator.mobile() }

    LaunchedEffect(Unit) {
        when (val result = geoLocation.current()) {
            is GeolocatorResult.Success -> {
                println("LOCATION: ${result.data.coordinates}")
                println(
                    "LOCATION NAME: ${
                        MobileGeocoder().placeOrNull(result.data.coordinates)?.locality
                    }"
                )
                viewModelprayer.fetchPrayerTimes(result.data.coordinates.latitude, result.data.coordinates.longitude)
            }

            is GeolocatorResult.Error -> {
                viewModelprayer.fetchPrayerTimes(21.3891, 39.8579)
            }
        }
    }

    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year

//    val selectedDateStatus by viewModel.getPrayerStatusForDate(selectedDate).collectAsState(initial = null)
    LaunchedEffect(selectedDate) {
        viewModel.setSelectedDate(selectedDate)
    }

    val selectedDateStatus by viewModel.selectedDayStatus

    val nextPrayerText = state?.let { prayerTimes ->
        val nextPrayer = viewModelprayer.getUpcomingPrayer(prayerTimes)
        nextPrayer?.let { (name, time) ->
            "$name at ${formatTo12HourKMP(time)}"
        } ?: "No upcoming prayer"
    } ?: "Loading..."

    val listItems = state?.let { prayerTimes ->
        listOf(
            Namazlist("FAJAR", Res.drawable.fajarpic, prayerTimes.fajr),
            Namazlist("ZUHR", Res.drawable.zuharpic, prayerTimes.dhuhr),
            Namazlist("ASR", Res.drawable.assarpic, prayerTimes.asr),
            Namazlist("MAGHRIB", Res.drawable.maghribpic, prayerTimes.maghrib),
            Namazlist("ISHA", Res.drawable.ishapic, prayerTimes.isha)
        )
    } ?: emptyList()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.arrowback),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onNext() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Daily Namaz Status", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        item {
            val monthDays = calendarData[currentMonthIndex]
            val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")


            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentMonthIndex > 0) currentMonthIndex--
                        },
                        enabled = currentMonthIndex > 0
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.calenderback),
                            contentDescription = "Previous Month",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "${Month.entries[currentMonthIndex].name} $currentYear",
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )

                    IconButton(
                        onClick = {
                            if (currentMonthIndex < calendarData.lastIndex) currentMonthIndex++
                        },
                        enabled = currentMonthIndex < calendarData.lastIndex
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.calendernext),
                            contentDescription = "Next Month",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(boxcolor)
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            weekDays.forEach { day ->
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.body2,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(top = 10.dp, bottom = 10.dp)
                                )
                            }
                        }

                        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(7),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(monthDays) { day ->
                                //for restrict future clickable date
                                val isFutureDate = day.date > Clock.System.todayIn(TimeZone.currentSystemDefault())

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                day.isToday -> greencolor
                                                day.date == selectedDate -> Color.Blue
                                                else -> Color.Transparent
                                            }
                                        )

                                    .clickable(enabled = !isFutureDate) {
                                        if (!isFutureDate) selectedDate = day.date
                                    },

//                                    .clickable { selectedDate = day.date },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.date.dayOfMonth.toString(),
                                        color = when {
                                            day.isToday -> Color.White
                                            day.date == selectedDate -> Color.White
                                            day.isCurrentMonth -> Color.Black
                                            else -> Color.LightGray
                                        },
                                        style = MaterialTheme.typography.body2,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                "Prayer Status For ${selectedDate.dayOfMonth} ${selectedDate.month.name} ${selectedDate.year}",
                style = MaterialTheme.typography.body1
            )
        }
        items(listItems) { (title, imageRes, subtitle) ->
//            val currentStatus = selectedDateStatus?.prayers?.get(title)?.status ?: PrayerStatus.NOT_OFFERED
fun mapToStorageKey(name: String): String {
    return when (name.uppercase()) {
        "FAJAR" -> "Fajr"
        "ZUHR" -> "Zuhr"
        "ASR" -> "Asr"
        "MAGHRIB" -> "Maghrib"
        "ISHA" -> "Isha"
        else -> name
    }
}
        val currentStatus = selectedDateStatus?.prayers?.get(mapToStorageKey(title))?.status ?: PrayerStatus.NOT_OFFERED

            Box(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(boxcolor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = "Prayer image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                        )
                        val timeAsLocalTime = LocalTime.parse(subtitle)
                        Text(
                            text = formatTo12HourKMP(timeAsLocalTime),
                            style = MaterialTheme.typography.body2
                        )
                    }

                    // Toggle button for prayer status
//                    PrayerStatusToggle(
//                        currentStatus = currentStatus,
//                        onStatusChanged = { newStatus ->
//                            viewModel.updatePrayerStatus(selectedDate.toString(), title, newStatus)
//                        }
//                    )

                    val isFutureDate = selectedDate > Clock.System.todayIn(TimeZone.currentSystemDefault())

                    PrayerStatusToggle(
                        currentStatus = currentStatus,
                        onStatusChanged = { newStatus ->
                            if (!isFutureDate) {
                                viewModel.updatePrayerStatus(selectedDate.toString(), title, newStatus)
                            }
                        },
                        enabled = !isFutureDate
                    )

                }
            }
        }
    }
}