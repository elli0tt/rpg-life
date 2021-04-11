package com.elli0tt.rpg_life.presentation.screen.quests.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.quests.QuestsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [QuestsComponent::class])
abstract class QuestsModule {

    @Binds
    @IntoMap
    @ViewModelKey(QuestsViewModel::class)
    abstract fun bindQuestsViewModel(viewModel: QuestsViewModel): ViewModel
}