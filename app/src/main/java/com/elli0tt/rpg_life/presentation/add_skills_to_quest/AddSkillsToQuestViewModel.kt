package com.elli0tt.rpg_life.presentation.add_skills_to_quest

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
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.GetQuestByIdUseCase
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteRelatedSkillUseCase
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.InsertRelatedSkillUseCase
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.UpdateQuestsUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.load_data.GetAddSkillsDataUseCase

class AddSkillsToQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val getAddSkillsDataUseCase: GetAddSkillsDataUseCase
    private val getQuestByIdUseCase: GetQuestByIdUseCase
    private val updateQuestsUseCase: UpdateQuestsUseCase
    private val insertRelatedSkillUseCase: InsertRelatedSkillUseCase
    private val deleteRelatedSkillUseCase: DeleteRelatedSkillUseCase

    private val skillsFromDB: LiveData<List<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    val skillsToShow: MediatorLiveData<MutableList<AddSkillData>> = MediatorLiveData()

    private var questId: MutableLiveData<Int> = MutableLiveData(0)

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        val questsRepository: QuestsRepository = QuestsRepositoryImpl(application)
        getAddSkillsDataUseCase = GetAddSkillsDataUseCase(skillsRepository, questsRepository)
        getQuestByIdUseCase = GetQuestByIdUseCase(questsRepository)
        updateQuestsUseCase = UpdateQuestsUseCase(questsRepository)
        insertRelatedSkillUseCase = InsertRelatedSkillUseCase(questsRepository)
        deleteRelatedSkillUseCase = DeleteRelatedSkillUseCase(questsRepository)

        skillsToShow.addSource(skillsFromDB) {
            skillsToShow.value = it as MutableList<AddSkillData>?
        }

    }

    fun start(questId: Int) {
        this.questId.value = questId
    }

    fun onSelectCheckBoxCheckChange(position: Int, isChecked: Boolean, xpPercentage: Int) {
        val skill = skillsToShow.value?.get(position)
        val skills = skillsToShow.value
        skills?.set(position, AddSkillData(skill!!.id, skill.name, isChecked, xpPercentage))
        skillsToShow.value = skills
    }

    fun onXpPercentageSeekBarTouchStop(position: Int, isChecked: Boolean, xpPercentage: Int) {
        val skill = skillsToShow.value?.get(position)
        val skills = skillsToShow.value
        skills?.set(position, AddSkillData(skill!!.id, skill.name, isChecked, xpPercentage))
        skillsToShow.value = skills
    }

    fun save() {
        val skills = skillsToShow.value
        if (skills != null) {
            for (skill in skills) {
                if (skill.isSelected) {
                    insertRelatedSkillUseCase.invoke(questId.value!!, skill.id, skill.xpPercentage)
                } else {
                    deleteRelatedSkillUseCase.invoke(questId.value!!, skill.id)
                }
            }
        }
    }

}