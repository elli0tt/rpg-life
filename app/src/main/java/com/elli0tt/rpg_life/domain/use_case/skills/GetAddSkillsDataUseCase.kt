package com.elli0tt.rpg_life.domain.use_case.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import javax.inject.Inject

class GetAddSkillsDataUseCase @Inject constructor(
    private val skillsRepository: SkillsRepository,
    private val questsRepository: QuestsRepository
) {

    private val addSkillData = MediatorLiveData<List<AddSkillData>>()
    private lateinit var allSkills: LiveData<List<Skill>>
    private lateinit var relatedSkills: LiveData<List<RelatedToQuestSkills>>
    private lateinit var questId: LiveData<Int>

    fun invoke(questId: LiveData<Int>): LiveData<List<AddSkillData>> {
        this.questId = questId
        allSkills = skillsRepository.allSkills

        relatedSkills = Transformations.switchMap(this.questId) { id ->
            questsRepository.getRelatedSkillsLiveData(id)
        }

        addSkillData.addSource(allSkills) { allSkills ->
            addSkillData.value = mapToAddSkillData(allSkills, relatedSkills.value)
        }

        addSkillData.addSource(relatedSkills) { relatedSkillsIds ->
            addSkillData.value = mapToAddSkillData(allSkills.value, relatedSkillsIds)
        }
        return addSkillData
    }

    private fun mapToAddSkillData(
        allSkills: List<Skill>?,
        relatedSkills: List<RelatedToQuestSkills>?
    ): MutableList<AddSkillData> {
        val resultList = ArrayList<AddSkillData>(allSkills?.size ?: 0)
        if (relatedSkills != null && allSkills != null) {
            for (skill in allSkills) {
                resultList.add(
                    AddSkillData(
                        skill.id,
                        skill.name,
                        getXpPercentage(relatedSkills, skill.id)
                    )
                )
            }
        }
        return resultList
    }


    private fun getXpPercentage(relatedSkills: List<RelatedToQuestSkills>, skillId: Int): Int {
        for (relatedSkill in relatedSkills) {
            if (skillId == relatedSkill.skillId) {
                return relatedSkill.xpPercentage
            }
        }
        return AddSkillData.DEFAULT_XP_PERCENT
    }
}