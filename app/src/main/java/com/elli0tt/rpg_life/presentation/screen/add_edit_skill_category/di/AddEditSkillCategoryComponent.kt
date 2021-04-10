package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.di

import com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.AddEditSkillsCategoryFragment
import dagger.Subcomponent

@AddEditSkillCategoryScope
@Subcomponent
interface AddEditSkillCategoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditSkillCategoryComponent
    }

    fun inject(fragment: AddEditSkillsCategoryFragment)
}