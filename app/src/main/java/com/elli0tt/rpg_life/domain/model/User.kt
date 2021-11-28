package com.elli0tt.rpg_life.domain.model

import kotlin.math.sqrt

data class User @JvmOverloads constructor(
    var name: String = DEFAULT_NAME,
    var totalXp: Int = DEFAULT_TOTAL_XP,
    var coinsCount: Int = DEFAULT_COINS_COUNT,
    var completedQuestsCount: Int = DEFAULT_COMPLETED_QUESTS_COUNT,
    var earnedCoinsCount: Int = DEFAULT_EARNED_COINS_COUNT
) {

    companion object {
        const val DEFAULT_NAME = ""
        const val DEFAULT_TOTAL_XP = 0
        const val DEFAULT_COINS_COUNT = 0
        const val DEFAULT_COMPLETED_QUESTS_COUNT = 0
        const val DEFAULT_EARNED_COINS_COUNT = 0
    }

    val level: Int
        get() = ((-1 + sqrt((1 + 8 * (totalXp / 500)).toDouble())) / 2).toInt()

    val xpLeftToNextLevel: Int
        get() = ((level + 1) * (level + 2)) / 2 * 500 - totalXp

    val xpToNextLevel: Int
        get() = ((level + 1) * (level + 2)) / 2 * 500 - (level * (level + 1)) / 2 * 500

}