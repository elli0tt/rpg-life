package com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.skills.GetAddSkillsDataUseCase

class AddSkillsToQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val getAddSkillsDataUseCase: GetAddSkillsDataUseCase

    private val skillsFromDB: LiveData<List<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    val skillsToShow: MediatorLiveData<MutableList<AddSkillData>> = MediatorLiveData()

    private var questId: MutableLiveData<Int> = MutableLiveData(0)

    private val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
    private val questsRepository: QuestsRepository = QuestsRepositoryImpl(application)

    init {
        getAddSkillsDataUseCase = GetAddSkillsDataUseCase(skillsRepository, questsRepository)

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