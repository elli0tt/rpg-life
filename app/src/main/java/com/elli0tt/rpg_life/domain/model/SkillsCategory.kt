package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skills_categories_table")
data class SkillsCategory(@PrimaryKey(autoGenerate = true) var id: Int = 0, var name: String = "") {
}