package com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.AddEditQuestFragment
import dagger.Subcomponent

@AddEditQuestScope
@Subcomponent
interface AddEditQuestComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditQuestComponent
    }

    fun inject(fragment: AddEditQuestFragment)
}