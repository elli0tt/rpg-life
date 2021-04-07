package com.elli0tt.rpg_life.di.modules.quests

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class QuestsRepositoryModule {

    @Binds
    abstract fun bindQuestRepository(questsRepositoryImpl: QuestsRepositoryImpl): QuestsRepository
}