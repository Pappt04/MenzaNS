package com.pappt04.menzans

import android.app.Application
import com.pappt04.menzans.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MenzaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MenzaApplication)
            modules(appModule)
        }
    }
}
