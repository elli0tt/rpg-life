package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di

import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.AddCategoryToSkillFragment
import dagger.Subcomponent

@AddCategoryToSkillScope
@Subcomponent
interface AddCategoryToSkillComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddCategoryToSkillComponent
    }

    fun inject(fragment: AddCategoryToSkillFragment)
}