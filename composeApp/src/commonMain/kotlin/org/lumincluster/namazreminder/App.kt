package org.lumincluster.namazreminder


import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.Notification.PermissionHandler

import org.lumincluster.namazreminder.Screens.CalendarScreen
import org.lumincluster.namazreminder.Screens.GetStartedScreen
import org.lumincluster.namazreminder.Screens.MainCalenderScreen
import org.lumincluster.namazreminder.Screens.NamazReminderScreen
import org.lumincluster.namazreminder.Screens.NamazStatusScreen
import org.lumincluster.namazreminder.Screens.SplashScreen
import org.lumincluster.namazreminder.ViewModel.CalendarViewModel
import org.lumincluster.namazreminder.ViewModel.PrayerViewModel

@Composable
fun App(permissionHandler: PermissionHandler,userId: Int) {
    MaterialTheme {


        var currentScreen by remember { mutableStateOf("splash") }
        var permissionGranted by remember { mutableStateOf<Boolean?>(null) }

        LaunchedEffect(Unit) {
            permissionGranted = permissionHandler.checkAndRequestPermission()
        }

        when (permissionGranted) {
            true -> {
                // Show main UI
            }
            false -> {
                // Show fallback or error
            }
            null -> {
                // Show loading/spinner
            }
        }
        // Automatically go to GetStarted screen after 3 seconds
        if (permissionGranted != null && currentScreen == "splash") {
        LaunchedEffect(Unit) {

                delay(3000)
                currentScreen = "getStarted"
            }
        }

        when (currentScreen) {
            "splash" -> SplashScreen()
            "getStarted" -> GetStartedScreen(onNext = { currentScreen = "namazstatus" })
            "namazstatus" -> NamazStatusScreen(onNext = { currentScreen = "calender" })
            "calender" -> MainCalenderScreen(onNext = { currentScreen = "namazstatus" })

//            "namazReminder" -> NamazReminderScreen()

        }

    }
}