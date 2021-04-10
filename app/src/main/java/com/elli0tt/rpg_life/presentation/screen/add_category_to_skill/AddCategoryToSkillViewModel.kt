package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill

import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import javax.inject.Inject

class AddCategoryToSkillViewModel @Inject constructor(
        private val skillsCategoriesRepository: SkillsCategoriesRepository,
        private val skillsRepository: SkillsRepository
) : ViewModel() {

    val skillsCategoriesToShow = skillsCategoriesRepository.getAllSkillsCategories()

    fun updateCategory(position: Int, skillId: Int) {
        skillsRepository.updateSkillCategoryById(skillId, skillsCategoriesToShow.value?.get(position)!!.id)
    }
}