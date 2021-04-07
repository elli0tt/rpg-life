package com.elli0tt.rpg_life.di.modules.skills_categories

import com.elli0tt.rpg_life.data.repository.SkillsCategoriesRepositoryImpl
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SkillsCategoriesRepositoryModule {

    @Binds
    abstract fun bindSkillsCategoriesRepository(
            skillsCategoriesRepositoryImpl: SkillsCategoriesRepositoryImpl
    ): SkillsCategoriesRepository
}