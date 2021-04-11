package com.elli0tt.rpg_life.presentation.screen.quests.di

import com.elli0tt.rpg_life.presentation.screen.quests.QuestsFragment
import dagger.Subcomponent

@QuestsScope
@Subcomponent
interface QuestsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): QuestsComponent
    }

    fun inject(fragment: QuestsFragment)
}