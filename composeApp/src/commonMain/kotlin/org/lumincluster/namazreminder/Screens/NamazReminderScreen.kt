package org.lumincluster.namazreminder.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import namazreminder.composeapp.generated.resources.arrowback
import namazreminder.composeapp.generated.resources.assarpic
import namazreminder.composeapp.generated.resources.donebtn
import namazreminder.composeapp.generated.resources.fajarpic
import namazreminder.composeapp.generated.resources.ishapic
import namazreminder.composeapp.generated.resources.maghribpic
import namazreminder.composeapp.generated.resources.playbtn
import namazreminder.composeapp.generated.resources.selectbtn
import namazreminder.composeapp.generated.resources.settingicon
import namazreminder.composeapp.generated.resources.sountbtn
import namazreminder.composeapp.generated.resources.vibrate
import namazreminder.composeapp.generated.resources.zuharpic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.models.Namazlist
import org.theme.boxcolor


@Composable
fun NamazReminderScreen() {
    val geoLocation = remember { Geolocator.mobile() }
    val viewModelPrayer = remember { PrayerViewModel() }
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

    val listItems = state?.let { prayerTimes ->
        listOf(
        Triple("Fajr Namaz", prayerTimes.fajr, Res.drawable.fajarpic),
        Triple("Zuhr Namaz", prayerTimes.dhuhr, Res.drawable.zuharpic),
        Triple("Asr Namaz", prayerTimes.asr, Res.drawable.assarpic),
        Triple("Maghrib Namaz", prayerTimes.maghrib, Res.drawable.maghribpic),
        Triple("Isha Namaz", prayerTimes.isha, Res.drawable.ishapic)
    )
    } ?: emptyList()
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {

        // Top Row: Back arrow, title and settings button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.arrowback),
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("Namaz Reminder", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.weight(1f))

            Box {
                Image(
                    painter = painterResource(Res.drawable.settingicon),
                    contentDescription = "Setting",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { menuExpanded = true }
                )

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .width(280.dp)
                ) {
                    // Row 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.vibrate),
                                contentDescription = null,

                            )
                            Spacer(Modifier.width(8.dp))
                            Text("vibrate on reminder")
                        }
                        Image(
                            painter = painterResource(Res.drawable.selectbtn),
                            contentDescription = null,

                        )
                    }

                    // Row 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(Res.drawable.sountbtn),
                                contentDescription = null,

                            )
                            Spacer(Modifier.width(8.dp))
                            Text("play sound on reminder")
                        }
                        Row {
                            Image(
                                painter = painterResource(Res.drawable.playbtn),
                                contentDescription = null,

                            )
                            Spacer(Modifier.width(8.dp))
                            Image(
                                painter = painterResource(Res.drawable.selectbtn),
                                contentDescription = null,

                            )
                        }
                    }

                    // Row 3
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.donebtn),
                            contentDescription = "Done",

                            )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vertical List
        LazyColumn {
            items(listItems) { (title, subtitle, imageRes) ->
                Box(
                    modifier = Modifier
                        .padding(vertical =  8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(boxcolor)


                )
                {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left image
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = title,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // Text
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, fontWeight = FontWeight.Bold)
                            val timeAsLocalTime = LocalTime.parse(subtitle)
                            Text(formatTo12HourKMP(timeAsLocalTime), style = MaterialTheme.typography.body2)
                        }

                        // Right icon
                        Image(
                            painter = painterResource(Res.drawable.selectbtn),
                            contentDescription = "select",
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }

                }
            }
        }
    }
}