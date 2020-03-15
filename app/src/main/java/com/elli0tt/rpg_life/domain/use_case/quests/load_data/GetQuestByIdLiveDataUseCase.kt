package com.elli0tt.rpg_life.domain.use_case.quests.load_data

import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.repository.QuestsRepository

class GetQuestByIdLiveDataUseCase(private val questsRepository: QuestsRepository) {
    fun invoke(questId: Int): LiveData<Quest>{
        return questsRepository.getQuestByIdLiveData(questId)
    }
}