package com.elli0tt.rpg_life.di.modules

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Module
class WorkManagerModule {

    @Provides
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}