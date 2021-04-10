package com.elli0tt.rpg_life.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase
import com.elli0tt.rpg_life.domain.model.SkillsCategory
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import javax.inject.Inject

class SkillsCategoriesRepositoryImpl @Inject constructor(context: Context) : SkillsCategoriesRepository {

    private val skillsCategoriesDao = AppRoomDatabase.getDatabase(context).skillsCategoriesDao

    override suspend fun insertSkillCategory(skillCategory: SkillsCategory) {
        skillsCategoriesDao.insertSkillsCategory(skillCategory)
    }

    override fun getAllSkillsCategories(): LiveData<List<SkillsCategory>> {
        return skillsCategoriesDao.getAllSkillsCategories()
    }

    override suspend fun getSkillCategoryById(skillCategoryId: Int): SkillsCategory {
        return skillsCategoriesDao.getSkillCategoryById(skillCategoryId)
    }

    override fun getSkillCategoryByIdSync(skillCategoryId: Int): SkillsCategory {
        return skillsCategoriesDao.getSkillCategoryByIdSync(skillCategoryId)
    }
}