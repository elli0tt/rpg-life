package com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.AddEditSkillViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddEditSkillComponent::class])
abstract class AddEditSkillModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditSkillViewModel::class)
    abstract fun bindAddEditSkillViewModel(viewModel: AddEditSkillViewModel): ViewModel
}