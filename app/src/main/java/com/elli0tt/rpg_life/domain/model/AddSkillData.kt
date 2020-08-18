package com.elli0tt.rpg_life.domain.model

data class AddSkillData(var id: Int, var name: String, var xpPercentage: Int) {
    companion object {
        const val DEFAULT_XP_PERCENT = 0
    }
}