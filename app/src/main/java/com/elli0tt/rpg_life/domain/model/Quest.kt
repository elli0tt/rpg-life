package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "quest_table")
data class Quest @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true) var id: Int = DEFAULT_ID,
    var name: String = DEFAULT_NAME,
    var description: String = DEFAULT_DESCRIPTION,
    var difficulty: Difficulty = DEFAULT_DIFFICULTY,
    var parentQuestId: Int = DEFAULT_PARENT_QUEST_ID,
    var isSubQuest: Boolean = DEFAULT_IS_SUBQUEST,
    var isImportant: Boolean = DEFAULT_IS_IMPORTANT,
    var isCompleted: Boolean = DEFAULT_IS_COMPLETED,
    var startDate: Calendar = DEFAULT_START_DATE,
    var dateDue: Calendar = DEFAULT_DATE_DUE,
    var startDateState: DateState = DEFAULT_START_DATE_STATE,
    var dateDueState: DateState = DEFAULT_DATE_DUE_STATE,
    var repeatState: RepeatState = DEFAULT_REPEAT_STATE,
    var hasSubquests: Boolean = DEFAULT_HAS_SUBQUESTS,
    var isChallenge: Boolean = DEFAULT_IS_CHALLENGE,
    var totalDaysCount: Int = DEFAULT_TOTAL_DAYS_COUNT,
    var dayNumber: Int = DEFAULT_DAY_NUMBER
) {

    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_NAME = ""
        const val DEFAULT_DESCRIPTION = ""

        @JvmField
        val DEFAULT_DIFFICULTY = Difficulty.NOT_SET
        const val DEFAULT_PARENT_QUEST_ID = 0
        const val DEFAULT_IS_SUBQUEST = false
        const val DEFAULT_IS_IMPORTANT = false
        const val DEFAULT_IS_COMPLETED = false

        @JvmField
        val DEFAULT_START_DATE: Calendar = Calendar.getInstance()

        @JvmField
        val DEFAULT_DATE_DUE: Calendar = Calendar.getInstance()

        @JvmField
        val DEFAULT_START_DATE_STATE = DateState.NOT_SET

        @JvmField
        val DEFAULT_DATE_DUE_STATE = DateState.NOT_SET

        @JvmField
        val DEFAULT_REPEAT_STATE = RepeatState.NOT_SET
        const val DEFAULT_HAS_SUBQUESTS = false
        const val DEFAULT_IS_CHALLENGE = false
        const val DEFAULT_TOTAL_DAYS_COUNT = 0
        const val DEFAULT_DAY_NUMBER = 0
    }

    /**
     * NOT_SET - hasn't been set yet
     * DATE_SET - only date is set
     * DATE_TIME_SET - date and time were set
     */
    enum class DateState {
        NOT_SET, DATE_SET, DATE_TIME_SET
    }

    enum class ReminderState {
        NOT_SET, PICK_CUSTOM_DATE
    }

    enum class RepeatState {
        DAILY, WEEKDAYS, WEEKENDS, WEEKLY, MONTHLY, YEARLY, CUSTOM, NOT_SET
    }

    override fun toString(): String {
        return "name: $name"
    }
}