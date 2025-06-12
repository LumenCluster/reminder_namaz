package org.lumincluster.namazreminder.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.jordond.compass.geocoder.MobileGeocoder
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.datetime.Clock

import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.grayrectangle
import namazreminder.composeapp.generated.resources.masjidicon
import namazreminder.composeapp.generated.resources.rectanglecalender
import org.jetbrains.compose.resources.painterResource
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.models.CalendarDay
import org.theme.boxcolor
import org.theme.calboxcolor
import org.theme.greencolor

@Composable
fun CalendarScreen(onNext: () -> Unit) {
    val viewModel = CalendarViewModel()
    val viewModelprayer = remember { PrayerViewModel() }
    val calendarData = viewModel.calendarDays
    val currentMonthIndex = remember {
        viewModel.calendarDays.indexOfFirst { month ->
            month.any { it.isToday }
        }
    }
    val pagerState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonthIndex)
    val state by viewModelprayer.state.collectAsState()
    val geoLocation = remember { Geolocator.mobile() }
    val currentYear = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .year
    LazyRow(
        state = pagerState,
        modifier = Modifier.fillMaxSize()


    ) {
        items(calendarData.size) { index ->
            val monthDays = calendarData[index]
            val weekDays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(360.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Month name on top
                Text(
                    text = "${Month.entries[index].name} $currentYear",
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Box for calendar
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // <-- This adds left/right spacing
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(boxcolor)
                        .padding(20.dp)
                ) {
                    Column {
                        // Weekdays header
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
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Divider
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Calendar grid
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(7),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(monthDays) { day ->
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                day.isToday -> Color.Red.copy(alpha = 0.2f)
                                                else -> Color.Transparent
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day.date.dayOfMonth.toString(),
                                        color = when {
                                            day.isToday ->  greencolor
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
    }
}