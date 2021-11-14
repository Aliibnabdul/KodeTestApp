package com.example.koder

import android.app.Application
import com.example.koder.di.networkModule
import com.example.koder.di.repoModule
import com.example.koder.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoderApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoderApp)
            modules(repoModule, vmModule, networkModule)
        }
    }
}