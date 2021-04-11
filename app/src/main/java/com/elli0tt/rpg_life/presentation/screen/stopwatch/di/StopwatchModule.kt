package com.elli0tt.rpg_life.presentation.screen.stopwatch.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.stopwatch.StopwatchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StopwatchModule {

    @Binds
    @IntoMap
    @ViewModelKey(StopwatchViewModel::class)
    abstract fun bindStopwatchViewModel(viewModel: StopwatchViewModel): ViewModel
}