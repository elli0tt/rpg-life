package com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di

import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.AddEditChallengeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddEditChallengeComponent::class])
abstract class AddEditChallengeModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditChallengeViewModel::class)
    abstract fun bindAddEditChallengeViewModel(viewModel: AddEditChallengeViewModel): AddEditChallengeViewModel
}