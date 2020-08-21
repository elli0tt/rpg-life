package com.elli0tt.rpg_life.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elli0tt.rpg_life.domain.model.SkillsCategory

@Dao
interface SkillsCategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkillsCategory(skillCategory: SkillsCategory)

    @Query("SELECT * FROM skills_categories_table ORDER BY name")
    fun getAllSkillsCategories(): LiveData<List<SkillsCategory>>

    @Query("SELECT * FROM skills_categories_table WHERE id = :skillCategoryId")
    suspend fun getSkillCategoryById(skillCategoryId: Int): SkillsCategory

    @Query("SELECT * FROM skills_categories_table WHERE id = :skillCategoryId")
    fun getSkillCategoryByIdSync(skillCategoryId: Int): SkillsCategory


}