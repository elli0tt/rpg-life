package com.elli0tt.rpg_life.presentation.add_edit_skill

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.SkillsRepository

class AddEditSkillViewModel(application: Application) : AndroidViewModel(application) {
    val name = MutableLiveData<String>()

    private lateinit var skill: Skill

    enum class Mode {
        ADD, EDIT
    }

    private var mode: Mode = Mode.ADD

    val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)

    fun start(skillId: Int) {
        if (skillId != 0) {
            mode = Mode.EDIT
            loadData(skillId)
        }
    }

    private fun loadData(skillId: Int) {
        object : Thread() {
            override fun run() {
                super.run()
                skill = skillsRepository.getSkillById(skillId)
                onDataLoaded()
            }
        }.start()
    }

    private fun onDataLoaded() {
        name.postValue(skill.name)
    }

    fun save() {
        when (mode) {
            Mode.ADD -> {
                skill = Skill(name = name.value ?: "")
                skillsRepository.insert(skill)
            }

            Mode.EDIT -> {
                skill.name = name.value ?: ""
                skillsRepository.update(skill)
            }
        }
    }

}