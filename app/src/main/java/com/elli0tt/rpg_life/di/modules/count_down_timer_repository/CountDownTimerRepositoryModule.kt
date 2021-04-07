package com.elli0tt.rpg_life.di.modules.count_down_timer_repository

import com.elli0tt.rpg_life.data.repository.CountDownTimerRepositoryImpl
import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CountDownTimerRepositoryModule {

    @Binds
    abstract fun bindCountDownTimerRepository(
            countDownTimerRepositoryImpl: CountDownTimerRepositoryImpl
    ): CountDownTimerRepository
}