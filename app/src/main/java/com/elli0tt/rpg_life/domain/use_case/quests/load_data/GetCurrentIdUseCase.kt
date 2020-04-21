package com.elli0tt.rpg_life.domain.use_case.quests.load_data

import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class GetCurrentIdUseCase(private val questsRepository: QuestsRepository) {
    fun invoke(): Int {
        return questsRepository.currentId
    }
}