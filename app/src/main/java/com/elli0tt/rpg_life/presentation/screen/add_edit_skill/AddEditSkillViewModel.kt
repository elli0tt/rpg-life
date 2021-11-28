package com.elli0tt.rpg_life.presentation.screen.add_edit_skill

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.model.SkillsCategory
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import javax.inject.Inject

class AddEditSkillViewModel @Inject constructor(
    private val skillsRepository: SkillsRepository,
    private val skillsCategoriesRepository: SkillsCategoriesRepository
) : ViewModel() {

    val name = MutableLiveData<String>()
    val skillsCategoryName = MutableLiveData<String>()

    private lateinit var skill: Skill
    private var skillCategory: SkillsCategory? = null

    val skillId
        get() = skill.id
    val skillName
        get() = name.value

    enum class Mode {
        ADD, EDIT, DELETED
    }

    private var mode: Mode = Mode.ADD

    private lateinit var defaultSkillCategoryText: String

    fun start(skillId: Int, defaultSkillCategoryText: String) {
        this.defaultSkillCategoryText = defaultSkillCategoryText

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
                if (skill.categoryId != 0) {
                    skillCategory =
                        skillsCategoriesRepository.getSkillCategoryByIdSync(skill.categoryId)
                }
                onDataLoaded()
            }
        }.start()
    }

    private fun onDataLoaded() {
        name.postValue(skill.name)
        skillsCategoryName.postValue(skillCategory?.name ?: defaultSkillCategoryText)
    }

    fun save() {
        when (mode) {
            Mode.ADD -> {
                skill = Skill(name = name.value ?: "")
                skillsRepository.insertSkills(skill)
            }

            Mode.EDIT -> {
                skill.name = name.value ?: ""
                skillsRepository.updateSkills(skill)
            }
            Mode.DELETED -> {
                /** Do nothing*/
            }
        }
    }

    fun delete() {
        if (mode == Mode.EDIT) {
            skillsRepository.deleteSkillsById(skillId)
        }
        mode = Mode.DELETED
    }
}