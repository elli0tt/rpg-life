package com.elli0tt.rpg_life.presentation.app

import android.app.Application
import com.elli0tt.rpg_life.BuildConfig
import com.elli0tt.rpg_life.di.AppComponent
import com.elli0tt.rpg_life.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initAppComponent()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }
}