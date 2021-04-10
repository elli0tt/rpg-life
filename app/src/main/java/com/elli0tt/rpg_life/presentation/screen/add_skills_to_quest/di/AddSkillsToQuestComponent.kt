package com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.di

import com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.AddSkillsToQuestFragment
import dagger.Subcomponent

@AddSkillsToQuestScope
@Subcomponent
interface AddSkillsToQuestComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddSkillsToQuestComponent
    }

    fun inject(fragment: AddSkillsToQuestFragment)
}