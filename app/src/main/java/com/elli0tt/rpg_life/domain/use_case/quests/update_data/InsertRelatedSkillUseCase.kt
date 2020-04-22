package com.elli0tt.rpg_life.domain.use_case.quests.update_data

import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class InsertRelatedSkillUseCase(private val questsRepository: QuestsRepository) {
    fun invoke(questId: Int, skillId: Int, xpPercentage: Int) {
        questsRepository.insertRelatedSkill(questId, skillId, xpPercentage)
    }
}