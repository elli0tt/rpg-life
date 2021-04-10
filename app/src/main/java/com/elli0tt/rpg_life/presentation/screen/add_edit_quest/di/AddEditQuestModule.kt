package com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.AddEditQuestViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddEditQuestComponent::class])
abstract class AddEditQuestModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditQuestViewModel::class)
    abstract fun bindAddEditQuestViewModel(viewModel: AddEditQuestViewModel): ViewModel
}