package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.AddEditCharacteristicFragment
import dagger.Subcomponent

@AddEditCharacteristicScope
@Subcomponent
interface AddEditCharacteristicComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditCharacteristicComponent
    }

    fun inject(fragment: AddEditCharacteristicFragment)
}