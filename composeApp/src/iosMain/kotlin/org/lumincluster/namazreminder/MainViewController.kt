package org.lumincluster.namazreminder

import androidx.compose.ui.window.ComposeUIViewController
import org.lumincluster.namazreminder.Notification.IOSPermissionHelper
import org.lumincluster.namazreminder.Notification.PermissionHandler
import org.lumincluster.namazreminder.Notification.PermissionHelper
import org.lumincluster.namazreminder.UniqueUserId.UserIdManagerImpl
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    val permissionHandler = PermissionHandler(IOSPermissionHelper())
    val userIdManager = UserIdManagerImpl()
    val userId = userIdManager.getUserId()
    val KeyValueStorageFactory = KeyValueStorageFactory.getInstance()
   // println("User ID: $userId")
    return ComposeUIViewController {
        App(permissionHandler,userId,KeyValueStorageFactory) // root @Composable with DI
    }
}