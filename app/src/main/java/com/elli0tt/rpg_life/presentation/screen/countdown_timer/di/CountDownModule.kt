package com.elli0tt.rpg_life.presentation.screen.countdown_timer.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.countdown_timer.CountDownViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [CountDownComponent::class])
abstract class CountDownModule {

    @Binds
    @IntoMap
    @ViewModelKey(CountDownViewModel::class)
    abstract fun bindCountDownViewModel(viewModel: CountDownViewModel): ViewModel
}