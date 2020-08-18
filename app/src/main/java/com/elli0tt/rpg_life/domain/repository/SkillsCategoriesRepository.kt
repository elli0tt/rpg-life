package com.elli0tt.rpg_life.domain.repository

import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.domain.model.SkillsCategory

interface SkillsCategoriesRepository {
    suspend fun insertSkillCategory(skillCategory: SkillsCategory)

    fun getAllSkillsCategories(): LiveData<List<SkillsCategory>>

    suspend fun getSkillCategoryById(skillCategoryId: Int): SkillsCategory

    fun getSkillCategoryByIdSync(skillCategoryId: Int): SkillsCategory
}