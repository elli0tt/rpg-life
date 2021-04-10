package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.di.modules.ViewModelModule
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.AddCategoryToSkillViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddCategoryToSkillComponent::class])
abstract class AddCategoryToSkillModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddCategoryToSkillViewModel::class)
    abstract fun bindAddCategoryToSkillViewModel(viewModel: AddCategoryToSkillViewModel): ViewModel
}