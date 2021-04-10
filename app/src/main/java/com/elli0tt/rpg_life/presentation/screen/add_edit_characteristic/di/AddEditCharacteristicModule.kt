package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.AddEditCharacteristicViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddEditCharacteristicComponent::class])
abstract class AddEditCharacteristicModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditCharacteristicViewModel::class)
    abstract fun bindAddEditCharacteristicViewModel(viewModel: AddEditCharacteristicViewModel): ViewModel
}