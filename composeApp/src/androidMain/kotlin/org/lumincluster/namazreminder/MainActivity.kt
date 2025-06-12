package org.lumincluster.namazreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import org.lumincluster.namazreminder.Notification.AndroidNamazNotificationScheduler
import org.lumincluster.namazreminder.Notification.AndroidPermissionHelper
import org.lumincluster.namazreminder.Notification.NamazViewModel
import org.lumincluster.namazreminder.Notification.PermissionHandler
import org.lumincluster.namazreminder.UniqueUserId.UserIdManagerImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ✅ Inject the Android Key-Value storage
        KeyValueStorageFactory.init(AndroidKeyValueStorage(applicationContext))
        val scheduler = AndroidNamazNotificationScheduler(applicationContext)
        val viewModel = NamazViewModel(scheduler)
        viewModel.scheduleDailyNamazNotifications()
       // NotificationPermissionHandlerProvider.init(this)
        val permissionHelper = AndroidPermissionHelper(this)
        val permissionHandler = PermissionHandler(permissionHelper)
        val userIdManager = UserIdManagerImpl(applicationContext)
        val userId = userIdManager.getUserId()
      //  Log.d("UserID", userId.toString())
        setContent {
//            App(permissionHandler,userId, storage = TODO())
            // FIX: Replace TODO() with the properly initialized storage instance.
            App(
                permissionHandler = permissionHandler,
                userId = userId,
                storage = KeyValueStorageFactory.getInstance() // <-- The fix
            )

        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray,
//        deviceId: Int
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
//        if (requestCode == 1001) {
//            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
//            if (granted) {
//                // You can notify shared ViewModel or state here
//                Log.d("Permission", "Notification permission granted")
//            } else {
//                Log.d("Permission", "Notification permission denied")
//            }
//        }
//    }

}

@Preview
@Composable
fun AppAndroidPreview() {

    //App()

}
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "namaz_channel",
            "Namaz Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}


