package org.lumincluster.namazreminder.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.getstartedbtn
import namazreminder.composeapp.generated.resources.namazgetstart
import org.jetbrains.compose.resources.painterResource
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.models.DailyPrayerStatus
import org.lumincluster.namazreminder.models.KeyValueStorage
import org.lumincluster.namazreminder.models.NamazUserStorage
import org.lumincluster.namazreminder.models.PrayerRecord
import org.lumincluster.namazreminder.models.PrayerStatus
import org.lumincluster.namazreminder.models.UserPrefStorage
import org.theme.getstartedone
import org.theme.getstartedtwo

@Composable
fun GetStartedScreen(onNext: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(getstartedone, getstartedtwo) // Blue gradient
                    )
                ),
            verticalArrangement = Arrangement.Top,

            ) {
            // Small Heading
            Text(
                text = "Namaz Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
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
                    .padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button

            Image(
                painter = painterResource(Res.drawable.getstartedbtn),
                contentDescription = "My SVG Icon",
                modifier = Modifier
                    .padding(start = 12.dp)
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
val greencolor = Color(0xFF149E5D)

@Composable
fun FirstTimeUserScreen(
    viewModel: CalendarViewModel,
    storage: KeyValueStorage,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }
    val allPrayers = listOf("Fajr", "Zuhr", "Asr", "Maghrib", "Isha")

    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    val prayerStatuses = remember {
        mutableStateMapOf<String, PrayerStatus>().apply {
            allPrayers.forEach { put(it, PrayerStatus.NOT_OFFERED) }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
//        color = greencolor

    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome",
                style = MaterialTheme.typography.h5,
                color = greencolor
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Let’s get started with your prayer tracking",
                style = MaterialTheme.typography.body1,
                color = greencolor
            )

            Spacer(Modifier.height(24.dp))

//            OutlinedTextField(
//                value = userName,
//                onValueChange = { userName = it },
//                label = { Text("Your Name") },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//            )
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = greencolor,
                    unfocusedBorderColor = greencolor.copy(alpha = 0.7f),
                    cursorColor = greencolor,
                    focusedLabelColor = greencolor,
                    unfocusedLabelColor = greencolor.copy(alpha = 0.7f)
                )
            )


            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Which prayers did you offer today?",
                        style = MaterialTheme.typography.subtitle1,
                        color = greencolor
                    )

                    Spacer(Modifier.height(12.dp))

                    allPrayers.forEach { prayer ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = prayer,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.weight(1f)
                            )
                            PrayerStatusToggle(
                                currentStatus = prayerStatuses[prayer] ?: PrayerStatus.NOT_OFFERED,
                                onStatusChanged = { newStatus ->
                                    prayerStatuses[prayer] = newStatus
                                },
                                enabled = true
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    scope.launch {
                        val initialPrayerStatus = allPrayers.associateWith { prayer ->
                            PrayerRecord(
                                time = "",
                                status = prayerStatuses[prayer] ?: PrayerStatus.NOT_OFFERED
                            )
                        }

                        val dailyStatus = DailyPrayerStatus(
                            name = userName,
                            date = today.toString(),
                            prayers = initialPrayerStatus
                        )

                        UserPrefStorage.saveUserName(storage, userName)
                        UserPrefStorage.markFirstTimeDone(storage)
                        NamazUserStorage.addUser(storage, dailyStatus)

                        viewModel.setSelectedDate(today)
                        onFinished()
                    }
                },
                enabled = userName.isNotBlank() &&
                        prayerStatuses.values.any { it != PrayerStatus.NOT_OFFERED },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = greencolor,
                    contentColor = Color.White
                )
            ) {
                Text("Start Tracking")
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
