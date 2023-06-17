package com.jakesilver.photoclient.scintillate

import android.app.Application
import com.jakesilver.photoclient.api.di.apiModule
import com.jakesilver.photoclient.scintillate.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    apiModule,
                    appModule,
                ),
            )
        }
    }
}