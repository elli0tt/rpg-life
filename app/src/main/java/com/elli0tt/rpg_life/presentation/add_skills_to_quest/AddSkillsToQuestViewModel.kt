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
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.GetQuestByIdUseCase
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetQuestByIdLiveDataUseCase
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.UpdateQuestsUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.GetAddSkillsDataUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.GetAllSkillsUseCase

class AddSkillsToQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val getAddSkillsDataUseCase: GetAddSkillsDataUseCase
    private val getQuestByIdUseCase: GetQuestByIdUseCase
    private val updateQuestsUseCase: UpdateQuestsUseCase

    val skillsToShow: LiveData<MutableList<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    private var questId = MutableLiveData<Int>(0)
    private lateinit var quest: Quest

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        val questsRepository:QuestsRepository = QuestsRepositoryImpl(application)
        getAddSkillsDataUseCase = GetAddSkillsDataUseCase(skillsRepository, questsRepository)
        getQuestByIdUseCase = GetQuestByIdUseCase(questsRepository)
        updateQuestsUseCase = UpdateQuestsUseCase(questsRepository)
    }

    fun start(questId: Int){
        this.questId.value = questId
        object : Thread() {
            override fun run() {
                super.run()
                quest = getQuestByIdUseCase.invoke(questId)
            }
        }.start()
    }

    fun onSelectCheckBoxCheckChange(position: Int, isChecked: Boolean){
        if (isChecked){
            quest.relatedSkillsIds.add(skillsToShow.value?.get(position)?.id)
        } else {
            quest.relatedSkillsIds.remove(skillsToShow.value?.get(position)?.id)
        }
        updateQuestsUseCase.invoke(quest)
    }
}