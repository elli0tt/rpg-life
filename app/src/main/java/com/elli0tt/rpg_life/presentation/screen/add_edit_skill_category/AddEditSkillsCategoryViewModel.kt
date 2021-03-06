package com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elli0tt.rpg_life.domain.model.SkillsCategory
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditSkillsCategoryViewModel @Inject constructor(
        private val skillsCategoryRepository: SkillsCategoriesRepository
) : ViewModel() {

    enum class Mode {
        ADD, EDIT
    }

    private var mode: Mode = Mode.ADD

    var name: MutableLiveData<String> = MutableLiveData("")

    private var categoryId: Int = 0

    fun start(categoryId: Int) {
        if (categoryId != 0) {
            this.categoryId = categoryId
            mode = Mode.EDIT
            viewModelScope.launch {
                val category = skillsCategoryRepository.getSkillCategoryById(categoryId)
                name.postValue(category.name)
            }
        }
    }

    fun save() {
        when (mode) {
            Mode.ADD -> {
                viewModelScope.launch {
                    val skillsCategory = SkillsCategory(name = name.value ?: "")
                    skillsCategoryRepository.insertSkillCategory(skillsCategory)
                }
            }
            Mode.EDIT -> {

            }
        }
    }
}