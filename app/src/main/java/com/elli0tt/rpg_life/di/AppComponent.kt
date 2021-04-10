package com.elli0tt.rpg_life.di

import android.content.Context
import com.elli0tt.rpg_life.di.modules.AppModule
import com.elli0tt.rpg_life.di.modules.RepositoryModule
import com.elli0tt.rpg_life.di.modules.ViewModelModule
import com.elli0tt.rpg_life.di.modules.WorkManagerModule
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillComponent
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di.AddEditChallengeComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di.AddEditChallengeModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di.AddEditCharacteristicComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di.AddEditCharacteristicModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di.AddEditQuestComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di.AddEditQuestModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di.AddEditSkillComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di.AddEditSkillModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    WorkManagerModule::class,
    AddCategoryToSkillModule::class,
    AddEditChallengeModule::class,
    AddEditCharacteristicModule::class,
    AddEditQuestModule::class,
    AddEditSkillModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun addCategoryToSkillComponentFactory(): AddCategoryToSkillComponent.Factory
    fun addEditChallengeComponentFactory(): AddEditChallengeComponent.Factory
    fun addEditCharacteristicComponentFactory(): AddEditCharacteristicComponent.Factory
    fun addEditQuestComponentFactory(): AddEditQuestComponent.Factory
    fun addEditSkillComponentFactory(): AddEditSkillComponent.Factory
}