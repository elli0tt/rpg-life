package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.elli0tt.rpg_life.domain.model.room_type_converters.CalendarConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.DateStateConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.DifficultyConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.RepeatStateConverter
import java.util.*

@Entity(tableName = "quest_table")
data class Quest @JvmOverloads constructor(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                 var name: String = "",
                 var description: String = "",
                 var difficulty: Difficulty = Difficulty.NOT_SET,
                 var parentQuestId: Int = 0,
                 var isSubQuest: Boolean = false,
                 var isImportant: Boolean = false,
                 var isCompleted: Boolean = false,
                 var startDate: Calendar = Calendar.getInstance(),
                 var dateDue: Calendar = Calendar.getInstance(),
                 var startDateState: DateState = DateState.NOT_SET,
                 var dateDueState: DateState = DateState.NOT_SET,
                 var repeatState: RepeatState = RepeatState.NOT_SET,
                 var hasSubquests: Boolean = false,
                 var isChallenge: Boolean = false,
                 var totalDaysCount: Int = 0,
                 var dayNumber: Int = 0) {
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