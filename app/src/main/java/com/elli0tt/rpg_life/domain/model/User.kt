package com.elli0tt.rpg_life.domain.model

data class User @JvmOverloads constructor(
        var name: String = DEFAULT_NAME,
        var level: Int = DEFAULT_LEVEL,
        var totalXp: Int = DEFAULT_TOTAL_XP,
        var coinsCount: Int = DEFAULT_COINS_COUNT,
        var completedQuestsCount: Int = DEFAULT_COMPLETED_QUESTS_COUNT) {

    companion object {
        const val DEFAULT_NAME = ""
        const val DEFAULT_LEVEL = 0
        const val DEFAULT_TOTAL_XP = 0
        const val DEFAULT_COINS_COUNT = 0
        const val DEFAULT_COMPLETED_QUESTS_COUNT = 0
    }

}