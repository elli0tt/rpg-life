package com.elli0tt.rpg_life.presentation.screen.character.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.character.CharacterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [CharacterComponent::class])
abstract class CharacterModule {

    @Binds
    @IntoMap
    @ViewModelKey(CharacterViewModel::class)
    abstract fun bindCharacterViewModel(viewModel: CharacterViewModel): ViewModel
}