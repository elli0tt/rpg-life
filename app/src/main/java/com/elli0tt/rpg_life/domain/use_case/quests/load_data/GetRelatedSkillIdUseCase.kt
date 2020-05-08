package com.elli0tt.rpg_life.domain.use_case.quests.load_data

import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class GetRelatedSkillIdUseCase(private val questsRepository: QuestsRepository) {
    fun invoke(questId: Int): Int {
        return questsRepository.getRelatedSkillId(questId)
    }
}