package org.lumincluster.namazreminder

import android.app.Application
import android.content.Context

lateinit var appContext: Context
class MainApp  : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}