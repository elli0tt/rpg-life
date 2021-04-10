package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.di

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.presentation.core.viewmodel.ViewModelKey
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.AddEditSkillsCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(subcomponents = [AddEditSkillsCategoryComponent::class])
abstract class AddEditSkillsCategoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditSkillsCategoryViewModel::class)
    abstract fun bindAddEditSkillCategoryViewModel(viewModel: AddEditSkillsCategoryViewModel): ViewModel
}