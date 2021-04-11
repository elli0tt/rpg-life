package com.elli0tt.rpg_life.presentation.screen.skills.di

import com.elli0tt.rpg_life.presentation.screen.skills.SkillsFragment
import dagger.Subcomponent

@SkillsScope
@Subcomponent
interface SkillsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SkillsComponent
    }

    fun inject(fragment: SkillsFragment)
}