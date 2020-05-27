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

    @Query("select skillId from related_to_quests_skills where questId = :questId")
    fun getRelatedSkillId(questId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRelatedSkill(relatedToQuestsSkills: RelatedToQuestSkills)

    @Query("delete from related_to_quests_skills where questId = :questId and skillId = :skillId")
    fun deleteRelatedSkill(questId: Int, skillId: Int)

    @Query("INSERT INTO related_to_quests_skills SELECT :newQuestId, skillId, xpPercentage FROM related_to_quests_skills WHERE questId = :oldQuestId")
    fun copyRelatedSkills(oldQuestId: Int, newQuestId: Int)
}