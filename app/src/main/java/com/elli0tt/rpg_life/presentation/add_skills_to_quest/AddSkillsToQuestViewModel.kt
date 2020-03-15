package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.app.Application
import androidx.lifecycle.*
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetQuestByIdLiveDataUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.GetAddSkillsDataUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.GetAllSkillsUseCase

class AddSkillsToQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val getAddSkillsDataUseCase: GetAddSkillsDataUseCase

    val skillsToShow: LiveData<MutableList<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    private var questId = MutableLiveData<Int>(0)

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        val questsRepository:QuestsRepository = QuestsRepositoryImpl(application)
        getAddSkillsDataUseCase = GetAddSkillsDataUseCase(skillsRepository, questsRepository)
    }

    fun start(questId: Int){
        this.questId.value = questId
    }
}