package org.lumincluster.namazreminder.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.getstartedbtn
import namazreminder.composeapp.generated.resources.namazgetstart
import org.jetbrains.compose.resources.painterResource
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.data.PrayerApi
import org.theme.getstartedone
import org.theme.getstartedtwo

@Composable
fun GetStartedScreen(onNext: () -> Unit) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( brush = Brush.verticalGradient(
                colors = listOf(getstartedone,getstartedtwo) // Blue gradient
            )),
        verticalArrangement = Arrangement.Top,

        ) {
        // Small Heading
        Text(
            text = "Namaz Status",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .padding(start=12.dp, top= 12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Large Heading
        Text(
            text = "Stay\nConnected To\nYour Faith",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            lineHeight = 38.sp, // Adjust this value as needed
            modifier = Modifier
                .padding(start=12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button

        Image(
            painter = painterResource(Res.drawable.getstartedbtn),
            contentDescription = "My SVG Icon",
            modifier = Modifier
                .padding(start=12.dp)
                .clickable { onNext() }

        )


        Spacer(modifier = Modifier.height(24.dp))

        // Image (replace with your actual image resource)
        Image(
            painter = painterResource(Res.drawable.namazgetstart), // Replace with your image ID
            contentDescription = "namaz Image",
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),

            contentScale = ContentScale.Crop
        )

    }
}