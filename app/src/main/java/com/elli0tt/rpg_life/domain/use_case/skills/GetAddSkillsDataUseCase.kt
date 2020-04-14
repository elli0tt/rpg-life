package com.elli0tt.rpg_life.domain.use_case.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetRelatedSkillsIdsLiveDataUseCase

class GetAddSkillsDataUseCase(private val skillsRepository: SkillsRepository,
                              private val questsRepository: QuestsRepository) {

    private val addSkillData = MediatorLiveData<List<AddSkillData>>()
    private lateinit var allSkills: LiveData<List<Skill>>
    private lateinit var relatedSkillsIds: LiveData<List<Int>>
    private lateinit var questId: LiveData<Int>

    fun invoke(questId: LiveData<Int>): LiveData<List<AddSkillData>> {
        this.questId = questId
        val getAllSkillsUseCase = GetAllSkillsUseCase(skillsRepository)
        allSkills = getAllSkillsUseCase.invoke()

        val getRelatedSkillsIdsUseCase = GetRelatedSkillsIdsLiveDataUseCase(questsRepository)
        relatedSkillsIds = Transformations.switchMap(this.questId){ questId ->
            getRelatedSkillsIdsUseCase.invoke(questId)
        }

        addSkillData.addSource(allSkills) { allSkills ->
            addSkillData.value = mapToAddSkillData(allSkills, relatedSkillsIds.value)
        }

        addSkillData.addSource(relatedSkillsIds) { relatedSkillsIds ->
            addSkillData.value = mapToAddSkillData(allSkills.value, relatedSkillsIds)
        }
        return addSkillData
    }

    private fun mapToAddSkillData(allSkills: List<Skill>?, relatedSkillsIds: List<Int>?): MutableList<AddSkillData> {
        val resultList = ArrayList<AddSkillData>(allSkills?.size ?: 0)
        if (relatedSkillsIds != null && allSkills != null) {
            for (skill in allSkills) {
                resultList.add(AddSkillData(skill.id, skill.name, relatedSkillsIds.contains(skill.id)))
            }
        }
        return resultList
    }
}