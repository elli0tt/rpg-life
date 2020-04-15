package com.elli0tt.rpg_life.presentation.add_edit_skill

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.InsertSkillsUseCase

class AddEditSkillViewModel(application: Application) : AndroidViewModel(application) {
    val name = MutableLiveData<String>()
    private val insertSkillsUseCase: InsertSkillsUseCase

    fun saveSkill() {
        val skillToSave = Skill(name = name.value ?: "")
        insertSkillsUseCase.invoke(skillToSave)
    }

    init {
        val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)
        insertSkillsUseCase = InsertSkillsUseCase(skillsRepository)
    }
}