package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity

@Entity(tableName = "related_to_quests_skills", primaryKeys = ["questId", "skillId"])
data class RelatedToQuestSkills(var questId: Int = 0,
                                var skillId: Int = 0) {
}