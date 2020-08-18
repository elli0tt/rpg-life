package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.sqrt

@Entity(tableName = "skills_table")
data class Skill @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Int = DEFAULT_ID,
        var name: String = DEFAULT_NAME,
        var timeSpentMillis: Long = DEFAULT_TIME_SPENT_MILLIS,
        var totalXp: Long = DEFAULT_TOTAL_XP,
        var categoryId: Int = DEFAULT_CATEGORY_ID) {

    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_NAME = ""
        const val DEFAULT_TIME_SPENT_MILLIS = 0L
        const val DEFAULT_TOTAL_XP = 0L
        const val DEFAULT_CATEGORY_ID = 0
    }

    val level: Long
        get() = ((-1 + sqrt((1 + 8 * (totalXp / 500)).toDouble())).toLong()) / 2

    val xpLeftToNextLevel: Long
        get() = ((level + 1) * (level + 2)) / 2 * 500 - totalXp

    val xpToNextLevel: Long
        get() = ((level + 1) * (level + 2)) / 2 * 500 - (level * (level + 1)) / 2 * 500

    val bonusForLevel: Long
        get() = level
}