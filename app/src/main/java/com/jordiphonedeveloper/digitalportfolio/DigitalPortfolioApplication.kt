package com.jordiphonedeveloper.digitalportfolio

import android.app.Application
import com.jordiphonedeveloper.digitalportfolio.feature.profile.di.profileFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DigitalPortfolioApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DigitalPortfolioApplication)
            modules(appModule, profileFeatureModule)
        }
    }
}
