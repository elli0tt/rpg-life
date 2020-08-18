package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity

@Entity(tableName = "related_to_quests_skills", primaryKeys = ["questId", "skillId"])
data class RelatedToQuestSkills @JvmOverloads constructor(var questId: Int = DEFAULT_QUEST_ID,
                                                          var skillId: Int = DEFAULT_SKILL_ID,
                                                          var xpPercentage: Int = DEFAULT_XP_PERCENTAGE) {
    companion object {
        const val DEFAULT_QUEST_ID = 0
        const val DEFAULT_SKILL_ID = 0
        const val DEFAULT_XP_PERCENTAGE = 100
    }
}