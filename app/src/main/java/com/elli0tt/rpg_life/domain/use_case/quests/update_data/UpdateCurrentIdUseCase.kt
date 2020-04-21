package com.elli0tt.rpg_life.domain.use_case.quests.update_data

import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class UpdateCurrentIdUseCase(private val questsRepository: QuestsRepository) {
    fun invoke() {
        questsRepository.currentId += 1
    }
}