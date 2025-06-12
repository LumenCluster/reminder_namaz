package org.lumincluster.namazreminder.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import namazreminder.composeapp.generated.resources.Res
import namazreminder.composeapp.generated.resources.compose_multiplatform
import namazreminder.composeapp.generated.resources.namazsplashpic
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.KeyValueStorageFactory
import org.lumincluster.namazreminder.models.KeyValueStorage
import org.lumincluster.namazreminder.models.NamazUserKV
import org.lumincluster.namazreminder.models.NamazUserStorage
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
                val user = NamazUserKV(
                    id = 0,
                    date = yesterdayStr,
                    fajr = "Not Added",
                    zuhr = "Not Added",
                    asr = "Not Added",
                    maghrib = "Not Added",
                    isha = "Not Added"
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


//@Preview
//@Composable
//fun SplashScreen() {
//    val storage = remember { KeyValueStorageFactory.getInstanceOrNull() }
//
//    LaunchedEffect(storage) {
//        storage?.let {
//            val user = NamazUserKV(
//                id = 0,
//                date = "2025-05-23",
//                fajr = "Not Added", zuhr = "Not Added", asr = "Not Added", maghrib = "Not Added", isha = "Not Added"
//            )
//            NamazUserStorage.addUser(it, user)
//            val users = NamazUserStorage.getUsers(it)
//            println("Namaz Users: $users")
//        } ?: println("⚠️ KeyValueStorage not initialized yet!")
//    }
//
//Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(Res.drawable.namazsplashpic), // Replace with your image resource
//            contentDescription = null,
//            contentScale = ContentScale.Crop, // Crop to fill the screen
//            modifier = Modifier.fillMaxSize()
//        )
//    }
//}