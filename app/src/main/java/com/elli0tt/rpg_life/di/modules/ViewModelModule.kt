package com.elli0tt.rpg_life.di.modules

import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}