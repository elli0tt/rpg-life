package com.elli0tt.rpg_life.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills

@Dao
interface RelatedToQuestsSkillsDao {

    @Query("select * from related_to_quests_skills where questId = :questId")
    fun getRelatedSkillsLiveData(questId: Int): LiveData<List<RelatedToQuestSkills>>

    @Query("select * from related_to_quests_skills where questId = :questId")
    fun getRelatedSkills(questId: Int): List<RelatedToQuestSkills>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(relatedToQuestsSkills: RelatedToQuestSkills)

    @Query("delete from related_to_quests_skills where questId = :questId and skillId = :skillId")
    fun delete(questId: Int, skillId: Int)
}