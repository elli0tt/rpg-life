package com.elli0tt.rpg_life.di

import com.elli0tt.rpg_life.di.modules.app.AppModule
import com.elli0tt.rpg_life.di.modules.count_down_timer_repository.CountDownTimerRepositoryModule
import com.elli0tt.rpg_life.di.modules.quests.QuestsRepositoryModule
import com.elli0tt.rpg_life.di.modules.skills.SkillsRepositoryModule
import com.elli0tt.rpg_life.di.modules.skills_categories.SkillsCategoriesRepositoryModule
import com.elli0tt.rpg_life.di.modules.user.UserRepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    CountDownTimerRepositoryModule::class,
    QuestsRepositoryModule::class,
    SkillsRepositoryModule::class,
    SkillsCategoriesRepositoryModule::class,
    UserRepositoryModule::class
])
interface AppComponent {

}