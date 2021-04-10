package com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.use_case.skills.GetAddSkillsDataUseCase
import javax.inject.Inject

class AddSkillsToQuestViewModel @Inject constructor(
        private val getAddSkillsDataUseCase: GetAddSkillsDataUseCase,
        private val questsRepository: QuestsRepository
) : ViewModel() {

    private val skillsFromDB: LiveData<List<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    val skillsToShow: MediatorLiveData<MutableList<AddSkillData>> = MediatorLiveData()

    private var questId: MutableLiveData<Int> = MutableLiveData(0)

    init {
        skillsToShow.addSource(skillsFromDB) {
            skillsToShow.value = it as MutableList<AddSkillData>?
        }
    }

    fun start(questId: Int) {
        this.questId.value = questId
    }

    fun onXpPercentageSeekBarTouchStop(position: Int, xpPercentage: Int) {
        val skill = skillsToShow.value?.get(position)
        val skills = skillsToShow.value
        skills?.set(position, AddSkillData(skill!!.id, skill.name, xpPercentage))
        skillsToShow.value = skills
    }

    fun save() {
        val skills = skillsToShow.value
        if (skills != null) {
            for (skill in skills) {
                if (skill.xpPercentage != AddSkillData.DEFAULT_XP_PERCENT) {
                    questsRepository.insertRelatedSkill(questId.value!!, skill.id, skill.xpPercentage)
                } else {
                    questsRepository.deleteRelatedSkill(questId.value!!, skill.id)
                }
            }
        }
    }

}