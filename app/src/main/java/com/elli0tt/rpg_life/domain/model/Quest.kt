package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.elli0tt.rpg_life.domain.model.room_type_converters.CalendarConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.DateStateConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.DifficultyConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.RepeatStateConverter
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase
import java.util.*

@Entity(tableName = "quest_table")
class Quest {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name = ""
    var description = ""

    @TypeConverters(DifficultyConverter::class)
    var difficulty = Difficulty.NOT_SET
    var parentQuestId = 0
    var isSubQuest = false

    @Ignore
    var rewards: List<Reward> = ArrayList()
    var isImportant = false
    var isCompleted = false

    @TypeConverters(CalendarConverter::class)
    var startDate: Calendar = Calendar.getInstance()

    @TypeConverters(CalendarConverter::class)
    var dateDue: Calendar = Calendar.getInstance()

    /**
     * NOT_SET - hasn't been set yet
     * DATE_SET - only date is set
     * DATE_TIME_SET - date and time were set
     */
    enum class DateState {
        NOT_SET, DATE_SET, DATE_TIME_SET
    }

    @TypeConverters(DateStateConverter::class)
    var startDateState = DateState.NOT_SET

    @TypeConverters(DateStateConverter::class)
    var dateDueState = DateState.NOT_SET

    enum class ReminderState {
        NOT_SET, PICK_CUSTOM_DATE
    }

    /**
     * NOT_SET - dateDue wasn't set yet
     * AFTER_DATE_DUE - current date is after dateDue (deadline is expired)
     * BEFORE_DATE_DUE - current date is before dateDue (deadline isn't expired)
     */
    enum class DateDueCurrentState {
        NOT_SET, AFTER_DATE_DUE, BEFORE_DATE_DUE, TODAY, TOMORROW
    }

    //    public void setDateDueState(DateDueState dateDueState){
//        this.dateDueState = dateDueState;
//    }

    //@TypeConverters({DateDueStateConverter.class})
    @Ignore
    private var dateDueCurrentState = DateDueCurrentState.NOT_SET

    enum class RepeatState {
        DAILY, WEEKDAYS, WEEKENDS, WEEKLY, MONTHLY, YEARLY, CUSTOM, NOT_SET
    }

    @TypeConverters(RepeatStateConverter::class)
    var repeatState = RepeatState.NOT_SET

    var hasSubquests: Boolean = false

    var isChallenge: Boolean = false
    var totalDaysCount: Int = 0
    var dayNumber: Int = 0

    @Ignore
    constructor(name: String,
                description: String,
                difficulty: Difficulty,
                rewards: List<Reward>,
                isImportant: Boolean) {
        this.name = name
        this.description = description
        this.difficulty = difficulty
        this.rewards = rewards
        this.isImportant = isImportant
    }

//    constructor() { // do nothing
//    }


    constructor(name: String) {
        this.name = name
    }

    @Ignore
    constructor(name: String, description: String, difficulty: Difficulty,
                dateDue: Calendar, dateDueCurrentState: DateDueCurrentState) {
        this.name = name
        this.description = description
        this.difficulty = difficulty
        this.dateDue = dateDue
        this.dateDueCurrentState = dateDueCurrentState
    }

    @Ignore
    constructor(id: Int, name: String, description: String,
                difficulty: Difficulty, dateDue: Calendar, isCompleted: Boolean,
                isImportant: Boolean //DateDueState dateDueState
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.difficulty = difficulty
        this.dateDue = dateDue
        this.isCompleted = isCompleted
        this.isImportant = isImportant
        //this.dateDueState = dateDueState;
    }

    fun getDateDueCurrentState(): DateDueCurrentState {
        if (dateDueState == DateState.NOT_SET) {
            return DateDueCurrentState.NOT_SET
        }
        val currentDate = Calendar.getInstance()
        if (areCalendarEquals(GetTodayCalendarUseCase().invoke(), dateDue)) {
            return DateDueCurrentState.TODAY
        }
        if (areCalendarEquals(GetTomorrowCalendarUseCase().invoke(), dateDue)) {
            return DateDueCurrentState.TOMORROW
        }
        return if (currentDate.after(dateDue)) {
            DateDueCurrentState.AFTER_DATE_DUE
        } else {
            DateDueCurrentState.BEFORE_DATE_DUE
        }
    }

    private fun areCalendarEquals(calendar1: Calendar, calendar2: Calendar): Boolean {
        return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR]
                && calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH]
                && calendar1[Calendar.DAY_OF_MONTH] == calendar2[Calendar.DAY_OF_MONTH]
                && calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY]
                && calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE]
    }

    override fun toString(): String {
        return "name: $name"
    }

    companion object {
        private const val COMPLETED_PERCENTAGE = 100

        @JvmStatic
        fun getDateDueFormatted(dateDue: Calendar): String {
            return String.format(Locale.getDefault(), "%02d.%02d.%04d %02d:%02d",
                    dateDue[Calendar.DAY_OF_MONTH],
                    dateDue[Calendar.MONTH] + 1,
                    dateDue[Calendar.YEAR],
                    dateDue[Calendar.HOUR_OF_DAY],
                    dateDue[Calendar.MINUTE])
        }
    }
}