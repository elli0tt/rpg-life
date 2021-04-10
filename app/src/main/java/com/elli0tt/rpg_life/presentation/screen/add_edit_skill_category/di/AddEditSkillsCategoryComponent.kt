package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.AddEditSkillsCategoryFragment
import dagger.Subcomponent

@AddEditSkillsCategoryScope
@Subcomponent
interface AddEditSkillsCategoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditSkillsCategoryComponent
    }

    fun inject(fragment: AddEditSkillsCategoryFragment)
}