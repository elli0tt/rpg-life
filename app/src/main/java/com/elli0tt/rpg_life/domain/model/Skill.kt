package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skills_table")
class Skill {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name = ""
    var timeSpentMillis: Long = 0
    var totalXp: Long = 0

    constructor() { //do nothing
    }

    constructor(name: String) {
        this.name = name
    }

    val level: Long
        get() = totalXp / 1000

    val xpLeftToNextLevel: Long
        get() = totalXp % 1000

    val xpPercentage: Int
        get() = xpLeftToNextLevel.toInt() / 10

    companion object {
        const val maxXpPercentage = 100
    }
}