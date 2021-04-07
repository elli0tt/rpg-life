package com.elli0tt.rpg_life.di.modules.skills

import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SkillsRepositoryModule {

    @Binds
    abstract fun bindSkillsRepository(skillsRepositoryImpl: SkillsRepositoryImpl): SkillsRepository
}