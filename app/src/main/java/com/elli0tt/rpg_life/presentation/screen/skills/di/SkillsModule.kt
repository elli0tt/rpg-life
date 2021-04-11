package com.elli0tt.rpg_life.presentation.screen.skills.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.skills.SkillsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SkillsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SkillsViewModel::class)
    abstract fun bindSkillsViewModel(viewModel: SkillsViewModel): ViewModel
}