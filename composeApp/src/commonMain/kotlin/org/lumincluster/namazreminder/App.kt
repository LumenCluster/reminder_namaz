package org.lumincluster.namazreminder


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.Notification.PermissionHandler

import org.lumincluster.namazreminder.Screens.CalendarScreen
import org.lumincluster.namazreminder.Screens.FirstTimeUserScreen
import org.lumincluster.namazreminder.Screens.GetStartedScreen
import org.lumincluster.namazreminder.Screens.MainCalenderScreen
import org.lumincluster.namazreminder.Screens.NamazReminderScreen
import org.lumincluster.namazreminder.Screens.NamazStatusScreen
import org.lumincluster.namazreminder.Screens.SplashScreen
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel
import org.lumincluster.namazreminder.models.KeyValueStorage
import org.lumincluster.namazreminder.models.UserPrefStorage



import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun App(permissionHandler: PermissionHandler, userId: Int, storage: KeyValueStorage?) {
    MaterialTheme {
        // Handle the case where storage is not yet initialized
        if (storage == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Initializing...",
                    style = MaterialTheme.typography.h6
                )
            }
            return@MaterialTheme
        }

        var currentScreen by remember { mutableStateOf("splash") }
        var permissionGranted by remember { mutableStateOf<Boolean?>(null) }
        val showSetup = remember { mutableStateOf(UserPrefStorage.isFirstTime(storage)) }

        // Request permission

        // Show Splash, then move forward
        if (permissionGranted != null && currentScreen == "splash") {
            LaunchedEffect(Unit) {
                delay(3000)
                currentScreen = if (showSetup.value) "setup" else "getStarted"

            }
        }
        LaunchedEffect(Unit) {
            permissionGranted = permissionHandler.checkAndRequestPermission()
        }

        when {
            showSetup.value -> {
                FirstTimeUserScreen(
                    storage = storage ,
                    viewModel = remember { CalendarViewModel() },
                    onFinished = {
                        showSetup.value = false
                        currentScreen = "getStarted"
                    }
                )

            }

            else -> {
                when (currentScreen) {
                    "splash" -> SplashScreen()
                    "getStarted" -> GetStartedScreen(onNext = { currentScreen = "namazstatus" })
                    "namazstatus" -> NamazStatusScreen(onNext = { currentScreen = "calender" })
                    "calender" -> MainCalenderScreen(onNext = { currentScreen = "namazstatus" })
                    "namazReminder" -> NamazReminderScreen()

                }
            }
        }
    }
}
