package com.elli0tt.rpg_life.domain.use_case.quests.load_data

import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills
import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class GetRelatedSkillsIdsUseCase(private val questsRepository: QuestsRepository) {
    fun invoke(questId: Int): List<RelatedToQuestSkills>{
        return questsRepository.getRelatedSkills(questId)
    }
}