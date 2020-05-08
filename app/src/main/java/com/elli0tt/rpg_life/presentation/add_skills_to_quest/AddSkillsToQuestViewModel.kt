package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    val skillsToShow: LiveData<List<AddSkillData>>
        get() = getAddSkillsDataUseCase.invoke(questId)

    private var questId: MutableLiveData<Int> = MutableLiveData(0)
    //position and xpPercentage
    private val selectedXpPercentages = ArrayList<Pair<Int, Int>>()

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        val questsRepository: QuestsRepository = QuestsRepositoryImpl(application)
        getAddSkillsDataUseCase = GetAddSkillsDataUseCase(skillsRepository, questsRepository)
        getQuestByIdUseCase = GetQuestByIdUseCase(questsRepository)
        updateQuestsUseCase = UpdateQuestsUseCase(questsRepository)
        insertRelatedSkillUseCase = InsertRelatedSkillUseCase(questsRepository)
        deleteRelatedSkillUseCase = DeleteRelatedSkillUseCase(questsRepository)
    }

    fun start(questId: Int) {
        this.questId.value = questId
    }

    fun onSelectCheckBoxCheckChange(position: Int, isChecked: Boolean, xpPercentage: Int) {
        if (isChecked) {
            selectedXpPercentages.add(position to xpPercentage)
        } else {
            for (element in selectedXpPercentages){
                if (element.first == position){
                    selectedXpPercentages.remove(element)
                    break
                }
            }
        }
    }

    fun save(){
        for (element in selectedXpPercentages){
            insertRelatedSkillUseCase.invoke(questId.value!!, skillsToShow.value?.get(element.first)?.id!!, element.second)
        }
    }

}