package org.lumincluster.namazreminder.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import kotlinx.datetime.Month
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.models.CalendarDay

@Preview
@Composable
fun MonthCalendar(
    viewModel: CalendarViewModel
) {
    val calendarData = viewModel.calendarDays
    val currentMonthIndex = remember { viewModel.calendarDays.indexOfFirst { month ->
        month.any { it.isToday }
    } }
    val pagerState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonthIndex)

    LazyRow(state = pagerState, modifier = Modifier.fillMaxSize()) {
        items(calendarData.size) { index ->
            val monthDays = calendarData[index]
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
            ) {
                Text(
                    text = Month.entries[index].name,
                    style = MaterialTheme.typography.h1
                )
                LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                    items(monthDays) { day: CalendarDay ->
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            color = when {
                                day.isToday -> Color.Red
                                day.isCurrentMonth -> Color.Black
                                else -> Color.Gray
                            },
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }
    }
}