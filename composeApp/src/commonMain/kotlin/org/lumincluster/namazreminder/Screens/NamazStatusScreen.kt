package org.lumincluster.namazreminder.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.assarpic
import namazreminder.composeapp.generated.resources.calenderback
import namazreminder.composeapp.generated.resources.calendericon
import namazreminder.composeapp.generated.resources.fajarpic
import namazreminder.composeapp.generated.resources.gocalbth
import namazreminder.composeapp.generated.resources.greencircle
import namazreminder.composeapp.generated.resources.ishapic
import namazreminder.composeapp.generated.resources.maghribpic
import namazreminder.composeapp.generated.resources.masjidicon
import namazreminder.composeapp.generated.resources.namaztimepic
import namazreminder.composeapp.generated.resources.noticon
import namazreminder.composeapp.generated.resources.zuharpic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.Notification.NamazViewModel
import org.lumincluster.namazreminder.Notification.NotificationScheduler
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.models.Namazlist
import org.theme.bordercolor
import org.theme.boxcolor

@Preview
@Composable
fun NamazStatusScreen(onNext: () -> Unit) {
    val viewModel = CalendarViewModel()
    val geoLocation = remember { Geolocator.mobile() }
    val viewModelPrayer = remember { PrayerViewModel() }

    val calendarData = viewModel.calendarDays
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val formatted = formatTo12HourKMP(now)
    val state by viewModelPrayer.state.collectAsState()
    LaunchedEffect(Unit) {

        when (val result = geoLocation.current()) {
            is GeolocatorResult.Success -> {
                println("LOCATION: ${result.data.coordinates}")
                println(
                    "LOCATION NAME: ${
                        MobileGeocoder()
                            .placeOrNull(result.data.coordinates)?.locality
                    }"
                )
                viewModelPrayer.fetchPrayerTimes(result.data.coordinates.latitude,result.data.coordinates.longitude)
            }

            is GeolocatorResult.Error -> {
                viewModelPrayer.fetchPrayerTimes(21.3891,39.8579)


            }
        }


    }

    val nextPrayerText = state?.let { prayerTimes ->
        val nextPrayer = viewModelPrayer.getUpcomingPrayer(prayerTimes)
        nextPrayer?.let { (name, time) ->
            "$name at ${formatTo12HourKMP(time)}"
        } ?: "No upcoming prayer"
    } ?: "Loading..."

    val itemsList = state?.let { prayerTimes ->
        listOf(
            Namazlist("FAJAR", Res.drawable.fajarpic, prayerTimes.fajr),
            Namazlist("ZUHR", Res.drawable.zuharpic, prayerTimes.dhuhr),
            Namazlist("ASR", Res.drawable.assarpic, prayerTimes.asr),
            Namazlist("MAGHRIB", Res.drawable.maghribpic, prayerTimes.maghrib),
            Namazlist("ISHA", Res.drawable.ishapic, prayerTimes.isha)
        )
    } ?: emptyList()

    val initialMonthIndex = viewModel.calendarDays.indexOfFirst { month ->
        month.any { it.isToday }
    }
    var currentMonthIndex by remember { mutableStateOf(initialMonthIndex.coerceIn(0, calendarData.lastIndex)) }

    val pagerState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonthIndex)
    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year

//todays date
    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }







    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Top bar: Icon with text, and notification icon on right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.masjidicon),
                    contentDescription = "Small Icon",
                    // modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("Welcome To",fontSize = 18.sp)
                    Text("Namaz Status",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                }

            }

//            Image(
//                painter = painterResource(Res.drawable.noticon),
//                contentDescription = "notification Icon",
//
//                )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Text line
        Text("Keep A Track Of Your Daily Prayers",fontSize = 20.sp,
            lineHeight = 35.sp, // Adjust this value as needed

            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Background Image
            Image(
                painter = painterResource(Res.drawable.namaztimepic),
                contentDescription = "Main Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Centered Text
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { Text(
                text = "$formatted",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,

            )
                Text(
                    text = "$nextPrayerText",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,

                ) }


        }
        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal List
        LazyRow {
            items(itemsList) { item ->
                Column(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(item.topText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Image(
                        painter = painterResource(item.imageResId),
                        contentDescription = item.topText,
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val timeAsLocalTime = LocalTime.parse(item.bottomText)
                    Text(formatTo12HourKMP(timeAsLocalTime))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rectangle with 2 text lines and list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = bordercolor, // Replace with any color
                    shape = RoundedCornerShape(12.dp)
                )
                .background(boxcolor, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text("PRAYER TRACKER")
            Text("Today, ${today.dayOfMonth}th ${today.month.name} ${today.year}")

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // Replace with any color
                    .padding(8.dp)
            ) {
                items(5) {
                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .padding(end = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.greencircle),
                            contentDescription = "Previous Month",
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Item Text", textAlign = TextAlign.Center)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Another rectangle with icon, two text lines, and button image
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = bordercolor, // Replace with any color
                    shape = RoundedCornerShape(12.dp)
                )
                .background(boxcolor, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.calendericon),

                    contentDescription = "calender Icon",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Daily Namaz Status", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("log your daily prayers so you never miss any prayers.")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(Res.drawable.gocalbth),
                contentDescription = "calender btn",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onNext() }

            )
        }
    }
}



fun formatTo12HourKMP(time: LocalTime): String {
    val hour12 = if (time.hour % 12 == 0) 12 else time.hour % 12
    val amPm = if (time.hour < 12) "AM" else "PM"
    val minute = time.minute.toString().padStart(2, '0')
    return "$hour12:$minute $amPm"
}

