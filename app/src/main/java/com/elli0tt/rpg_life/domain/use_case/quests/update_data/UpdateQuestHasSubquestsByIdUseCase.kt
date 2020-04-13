package com.elli0tt.rpg_life.domain.use_case.quests.update_data

import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class UpdateQuestHasSubquestsByIdUseCase(private val questsRepository: QuestsRepository){
    fun invoke(id: Int, hasSubquests: Boolean){
        questsRepository.updateQuestHasSubquestsById(id, hasSubquests)
    }
}