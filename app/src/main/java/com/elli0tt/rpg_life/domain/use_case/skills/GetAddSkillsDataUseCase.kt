package com.elli0tt.rpg_life.domain.use_case.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.model.Quest
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetQuestByIdLiveDataUseCase

class GetAddSkillsDataUseCase(private val skillsRepository: SkillsRepository,
                              private val questsRepository: QuestsRepository) {

    private val addSkillData = MediatorLiveData<MutableList<AddSkillData>>()
    private lateinit var allSkills: LiveData<List<Skill>>
    private lateinit var quest: LiveData<Quest>

    fun invoke(questId: LiveData<Int>): LiveData<MutableList<AddSkillData>> {
        val getAllSkillsUseCase = GetAllSkillsUseCase(skillsRepository)
        allSkills = getAllSkillsUseCase.invoke()
        val getQuestByIdLiveDataUseCase = GetQuestByIdLiveDataUseCase(questsRepository)
        quest = Transformations.switchMap(questId){ id ->
            getQuestByIdLiveDataUseCase.invoke(id)
        }

        addSkillData.addSource(allSkills) { allSkillsList ->
            addSkillData.value = mapToAddSkillData(allSkillsList, quest.value)
        }

        addSkillData.addSource(quest) {quest ->
            addSkillData.value = mapToAddSkillData(allSkills.value, quest)
        }

        return addSkillData
    }

    private fun mapToAddSkillData(allSkills: List<Skill>?, quest: Quest?): MutableList<AddSkillData> {
        val resultList = ArrayList<AddSkillData>(allSkills?.size ?: 0)
        if (quest != null && allSkills != null) {
            for (skill in allSkills) {
                resultList.add(AddSkillData(skill.id, skill.name, quest.relatedSkillsIds.contains(skill.id)))
            }
        }
        return resultList
    }
}