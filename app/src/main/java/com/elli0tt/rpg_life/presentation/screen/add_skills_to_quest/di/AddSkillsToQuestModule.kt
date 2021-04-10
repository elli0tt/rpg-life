package com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.AddSkillsToQuestViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddSkillsToQuestModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddSkillsToQuestViewModel::class)
    abstract fun bindAddSkillsToQuestViewModel(viewModel: AddSkillsToQuestViewModel): ViewModel
}