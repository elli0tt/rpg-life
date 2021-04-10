package com.elli0tt.rpg_life.di

import android.content.Context
import com.elli0tt.rpg_life.di.modules.AppModule
import com.elli0tt.rpg_life.di.modules.RepositoryModule
import com.elli0tt.rpg_life.di.modules.ViewModelModule
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillComponent
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    AddCategoryToSkillModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun addCategoryToSkillComponent(): AddCategoryToSkillComponent.Factory
}