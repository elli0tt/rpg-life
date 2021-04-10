package com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.AddEditSkillFragment
import dagger.Subcomponent

@AddEditSkillScope
@Subcomponent
interface AddEditSkillComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditSkillComponent
    }

    fun inject(fragment: AddEditSkillFragment)
}