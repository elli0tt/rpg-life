package com.elli0tt.rpg_life.presentation.add_category_to_skill

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.elli0tt.rpg_life.data.repository.SkillsCategoriesRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository

class AddCategoryToSkillViewModel(application: Application) : AndroidViewModel(application) {
    private val skillsCategoriesRepository: SkillsCategoriesRepository = SkillsCategoriesRepositoryImpl(application)
    private val skillsRepository: SkillsRepository = SkillsRepositoryImpl(application)

    val skillsCategoriesToShow = skillsCategoriesRepository.getAllSkillsCategories()

    fun updateCategory(position: Int, skillId: Int){
        skillsRepository.updateSkillCategoryById(skillId, skillsCategoriesToShow.value?.get(position)!!.id)
    }
}