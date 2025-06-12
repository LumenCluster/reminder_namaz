package org.lumincluster.namazreminder.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.namazsplashpic
import org.jetbrains.compose.resources.painterResource
import org.lumincluster.namazreminder.KeyValueStorageFactory
import org.lumincluster.namazreminder.models.DailyPrayerStatus
import org.lumincluster.namazreminder.models.NamazUserStorage
import org.lumincluster.namazreminder.models.PrayerRecord
import org.lumincluster.namazreminder.models.PrayerStatus


@Composable
fun SplashScreen() {
    val storage = remember { KeyValueStorageFactory.getInstanceOrNull() }

    val yesterday = Clock.System.todayIn(TimeZone.currentSystemDefault())
        .minus(1, DateTimeUnit.DAY)

    LaunchedEffect(storage) {
        storage?.let {
            val yesterdayStr = yesterday.toString()
            val alreadyExists = NamazUserStorage.getUsers(it)
                .any { user -> user.date == yesterdayStr }

            if (!alreadyExists) {
                // Create a new DailyPrayerStatus with default values
                val user = DailyPrayerStatus(
                    name = "",
                    date = yesterdayStr,
                    prayers = mapOf(
                        "Fajr" to PrayerRecord("", PrayerStatus.NOT_OFFERED),
                        "Zuhr" to PrayerRecord("", PrayerStatus.NOT_OFFERED),
                        "Asr" to PrayerRecord("", PrayerStatus.NOT_OFFERED),
                        "Maghrib" to PrayerRecord("", PrayerStatus.NOT_OFFERED),
                        "Isha" to PrayerRecord("", PrayerStatus.NOT_OFFERED)
                    )
                )
                NamazUserStorage.addUser(it, user)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.namazsplashpic),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}