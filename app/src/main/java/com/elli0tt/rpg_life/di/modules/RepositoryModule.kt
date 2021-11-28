package com.elli0tt.rpg_life.di.modules

import com.elli0tt.rpg_life.data.repository.*
import com.elli0tt.rpg_life.domain.repository.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindCountDownTimerRepository(
        countDownTimerRepositoryImpl: CountDownTimerRepositoryImpl
    ): CountDownTimerRepository

    @Binds
    abstract fun bindQuestRepository(questsRepositoryImpl: QuestsRepositoryImpl): QuestsRepository

    @Binds
    abstract fun bindSkillsRepository(skillsRepositoryImpl: SkillsRepositoryImpl): SkillsRepository

    @Binds
    abstract fun bindSkillsCategoriesRepository(
        skillsCategoriesRepositoryImpl: SkillsCategoriesRepositoryImpl
    ): SkillsCategoriesRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}