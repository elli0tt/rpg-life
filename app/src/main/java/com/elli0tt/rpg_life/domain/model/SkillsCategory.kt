package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skills_categories_table")
data class SkillsCategory(@PrimaryKey(autoGenerate = true) var id: Int = DEFAULT_ID, var name: String = DEFAULT_NAME) {

    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_NAME = ""
    }
}