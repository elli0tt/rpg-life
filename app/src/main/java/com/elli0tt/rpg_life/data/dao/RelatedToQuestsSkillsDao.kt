package com.elli0tt.rpg_life.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills

@Dao
interface RelatedToQuestsSkillsDao {

    @Query("select skillId from related_to_quests_skills where questId = :questId")
    fun getRelatedSkillsIdsLiveData(questId: Int): LiveData<List<Int>>

    @Query("select skillId from related_to_quests_skills where questId = :questId")
    fun getRelatedSkillsIds(questId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(relatedToQuestsSkills: RelatedToQuestSkills)

    @Query("delete from related_to_quests_skills where questId = :questId and skillId = :skillId")
    fun delete(questId: Int, skillId: Int)
}