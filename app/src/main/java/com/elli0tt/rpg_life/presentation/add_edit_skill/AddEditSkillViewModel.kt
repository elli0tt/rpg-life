package com.elli0tt.rpg_life.presentation.add_edit_skill

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.skills.load_data.GetSkillByIdUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.InsertSkillsUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.UpdateSkillsUseCase

class AddEditSkillViewModel(application: Application) : AndroidViewModel(application) {
    val name = MutableLiveData<String>()

    private val insertSkillsUseCase: InsertSkillsUseCase
    private val getSkillByIdUseCase: GetSkillByIdUseCase
    private val updateSkillsUseCase: UpdateSkillsUseCase

    private lateinit var skill: Skill

    enum class Mode {
        ADD, EDIT
    }

    private var mode: Mode = Mode.ADD

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        insertSkillsUseCase = InsertSkillsUseCase(skillsRepository)
        getSkillByIdUseCase = GetSkillByIdUseCase(skillsRepository)
        updateSkillsUseCase = UpdateSkillsUseCase(skillsRepository)
    }

    fun start(skillId: Int) {
        if (skillId != 0){
            mode = Mode.EDIT
            loadData(skillId)
        }
    }

    private fun loadData(skillId: Int){
        object : Thread(){
            override fun run() {
                super.run()
                skill = getSkillByIdUseCase.invoke(skillId)
                onDataLoaded()
            }
        }.start()
    }

    private fun onDataLoaded(){
        name.postValue(skill.name)
    }

    fun save() {
        when(mode){
            Mode.ADD -> {
                skill = Skill(name = name.value ?: "")
                insertSkillsUseCase.invoke(skill)
            }

            Mode.EDIT -> {
                skill.name = name.value ?: ""
                updateSkillsUseCase.invoke(skill)
            }
        }
    }

}