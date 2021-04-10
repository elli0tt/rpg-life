package com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.AddEditChallengeFragment
import dagger.Subcomponent

@AddEditChallengeScope
@Subcomponent
interface AddEditChallengeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditChallengeComponent
    }

    fun inject(fragment: AddEditChallengeFragment)
}