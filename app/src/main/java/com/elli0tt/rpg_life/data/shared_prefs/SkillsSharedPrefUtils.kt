package com.elli0tt.rpg_life.data.shared_prefs

import android.content.Context
import android.content.SharedPreferences
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState

class SkillsSharedPrefUtils(context: Context) {
    private val sharedPreferences: SharedPreferences

    private companion object {
        const val SHARED_PREFERENCES_NAME = "skills shared preferences"
        const val KEY_SKILLS_SORTING_STATE = "key skills sorting state"
    }

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE)
    }

    fun getQuestsSortingState(): SkillsSortingState {
        val ordinal = sharedPreferences.getInt(KEY_SKILLS_SORTING_STATE,
                SkillsSortingState.NAME_ASC.ordinal)
        return SkillsSortingState.values()[ordinal]
    }

    fun setQuestsSortingState(sortingState: SkillsSortingState) {
        sharedPreferences.edit().putInt(KEY_SKILLS_SORTING_STATE, sortingState.ordinal).apply()
    }
}