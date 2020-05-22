package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlin.math.sqrt

@Entity(tableName = "skills_table")
data class Skill @JvmOverloads constructor(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var name: String = "",
        var timeSpentMillis: Long = 0,
        var totalXp: Long = 0,
        var categoryId: Int = 0) {

    val level: Long
        get() = ((-1 + sqrt((1 + 8 * (totalXp / 1000)).toDouble())).toLong()) / 2

    val xpLeftToNextLevel: Long
        get() = ((level + 1) * (level + 2)) / 2 * 1000 - totalXp

    val xpToNextLevel: Long
        get() = ((level + 1) * (level + 2)) / 2 * 1000 - (level * (level + 1)) / 2 * 1000
}