package com.elli0tt.rpg_life.data.shared_prefs

import android.content.Context
import com.elli0tt.rpg_life.domain.model.User
import com.google.gson.Gson

class SharedPreferencesUtils(context: Context) {

    private val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private companion object {
        const val SHARED_PREFERENCES_NAME = "com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils"

        const val KEY_USER = "KEY_USER"
        const val KEY_TIME_LEFT_SECONDS = "time left seconds"
        const val KEY_END_TIME = "end time"
        const val KEY_TIMER_STATE = "is timer running"
        const val KEY_TIMER_LENGTH_SECONDS = "timer length seconds"
        const val KEY_IS_TIMER_NEW = "is timer new"
        const val KEY_QUESTS_FILTER_STATE = "key quests filter state"
        const val KEY_QUESTS_SORTING_STATE = "key quests sorting state"
        const val KEY_SHOW_COMPLETED = "key show completed"
        const val KEY_SKILLS_SORTING_STATE = "key skills sorting state"

        val DEFAULT_USER: String = Gson().toJson(User())
        const val DEFAULT_TIME_LEFT_SECONDS = 60000L
        const val DEFAULT_END_TIME = 0L
        val DEFAULT_TIMER_STATE = TimerState.STOPPED
        const val DEFAULT_TIME_LENGTH_SECONDS = 0L
        const val DEFAULT_IS_TIMER_NEW = true
        val DEFAULT_QUESTS_FILTER_STATE = QuestsFilterState.ALL
        val DEFAULT_QUESTS_SORTING_STATE = QuestsSortingState.NAME
        const val DEFAULT_IS_SHOW_COMPLETED = false
    }

    var user: User
        get() =
            Gson().fromJson(sharedPreferences.getString(KEY_USER, DEFAULT_USER), User::class.java)
        set(value) =
            sharedPreferences.edit().putString(KEY_USER, Gson().toJson(value)).apply()

    var timeLeftSeconds: Long
        get() = sharedPreferences.getLong(KEY_TIME_LEFT_SECONDS, DEFAULT_TIME_LEFT_SECONDS)
        set(value) = sharedPreferences.edit().putLong(KEY_TIME_LEFT_SECONDS, value).apply()

    var endTime: Long
        get() = sharedPreferences.getLong(KEY_END_TIME, DEFAULT_END_TIME)
        set(value) = sharedPreferences.edit().putLong(KEY_END_TIME, value).apply()

    var timerState: TimerState
        get() {
            val ordinal = sharedPreferences.getInt(KEY_TIMER_STATE, DEFAULT_TIMER_STATE.ordinal)
            return TimerState.values()[ordinal]
        }
        set(value) = sharedPreferences.edit().putInt(KEY_TIMER_STATE, value.ordinal).apply()

    var timerLengthSeconds: Long
        get() = sharedPreferences.getLong(KEY_TIMER_LENGTH_SECONDS, DEFAULT_TIME_LENGTH_SECONDS)
        set(value) = sharedPreferences.edit().putLong(KEY_TIMER_LENGTH_SECONDS, value).apply()

    var isTimerNew: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_TIMER_NEW, DEFAULT_IS_TIMER_NEW)
        set(value) = sharedPreferences.edit().putBoolean(KEY_IS_TIMER_NEW, value).apply()

    var questsFilterState: QuestsFilterState
        get() {
            val ordinal = sharedPreferences.getInt(KEY_QUESTS_FILTER_STATE,
                    DEFAULT_QUESTS_FILTER_STATE.ordinal)
            return QuestsFilterState.values()[ordinal]
        }
        set(value) = sharedPreferences.edit().putInt(KEY_QUESTS_FILTER_STATE, value.ordinal).apply()

    var questsSortingState: QuestsSortingState
        get() {
            val ordinal = sharedPreferences.getInt(KEY_QUESTS_SORTING_STATE,
                    DEFAULT_QUESTS_SORTING_STATE.ordinal)
            return QuestsSortingState.values()[ordinal]
        }
        set(value) = sharedPreferences.edit().putInt(KEY_QUESTS_SORTING_STATE, value.ordinal).apply()

    var isShowCompleted: Boolean
        get() = sharedPreferences.getBoolean(KEY_SHOW_COMPLETED, DEFAULT_IS_SHOW_COMPLETED)
        set(value) = sharedPreferences.edit().putBoolean(KEY_SHOW_COMPLETED, value).apply()

    var skillsSortingState: SkillsSortingState
        get() {
            val ordinal = sharedPreferences.getInt(KEY_SKILLS_SORTING_STATE,
                    SkillsSortingState.NAME_ASC.ordinal)
            return SkillsSortingState.values()[ordinal]
        }
        set(value) = sharedPreferences.edit().putInt(KEY_SKILLS_SORTING_STATE, value.ordinal).apply()
}