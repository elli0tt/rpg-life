package com.elli0tt.rpg_life.presentation.screen.character.di

import com.elli0tt.rpg_life.presentation.screen.character.CharacterFragment
import dagger.Subcomponent

@CharacterScope
@Subcomponent
interface CharacterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CharacterComponent
    }

    fun inject(fragment: CharacterFragment)
}