package com.elli0tt.rpg_life.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.elli0tt.rpg_life.domain.model.room_type_converters.CalendarConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.DifficultyConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.RelatedSkillsIdsConverter
import com.elli0tt.rpg_life.domain.model.room_type_converters.RepeatStateConverter
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase
import java.util.*

@Entity(tableName = "quest_table")
class Quest {
    enum class Difficulty(val xpIncrease: Int, val xpDecrease: Int, val procrastinationIncrease: Int, val procrastinationDecrease: Int) {
        VERY_EASY(200, 400, 1, 2),
        EASY(400, 800, 2, 4),
        NORMAL(1000, 2000, 3, 6),
        HARD(2000, 4000, 5, 10),
        VERY_HARD(10000, 20000, 10, 20),
        IMPOSSIBLE(30000, 60000, 30, 60),
        NOT_SET(0, 0, 0, 0);
    }

    @PrimaryKey(autoGenerate = true)
    var id = 0
    var name = ""
    var description = ""
    @TypeConverters(DifficultyConverter::class)
    var difficulty = Difficulty.NORMAL
    var parentQuestId = 0
    var isSubQuest = false
    @Ignore
    var rewards: List<Reward> = ArrayList()
    var isImportant = false
    var isCompleted = false
    @Ignore
    private val startDate: Calendar? = null
    @TypeConverters(CalendarConverter::class)
    var dateDue = Calendar.getInstance()

    /**
     * NOT_SET - dateDue wasn't set yet
     * AFTER_DATE_DUE - current date is after dateDue (deadline is expired)
     * BEFORE_DATE_DUE - current date is before dateDue (deadline isn't expired)
     */
    enum class DateDueState {
        NOT_SET, AFTER_DATE_DUE, BEFORE_DATE_DUE, TODAY, TOMORROW
    }

    //    public void setDateDueState(DateDueState dateDueState){
//        this.dateDueState = dateDueState;
//    }
    var isDateDueSet = false
    //@TypeConverters({DateDueStateConverter.class})
    @Ignore
    private var dateDueState = DateDueState.NOT_SET

    enum class RepeatState {
        DAILY, WEEKDAYS, WEEKENDS, WEEKLY, MONTHLY, YEARLY, CUSTOM, NOT_SET
    }

    @TypeConverters(RepeatStateConverter::class)
    var repeatState = RepeatState.NOT_SET

    var hasSubquests: Boolean = false

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
                dateDue: Calendar, dateDueState: DateDueState) {
        this.name = name
        this.description = description
        this.difficulty = difficulty
        this.dateDue = dateDue
        this.dateDueState = dateDueState
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

    fun complete() {}

    fun getDateDueState(): DateDueState {
        if (!isDateDueSet) {
            return DateDueState.NOT_SET
        }
        val currentDate = Calendar.getInstance()
        if (areCalendarEquals(GetTodayCalendarUseCase().invoke(), dateDue)) {
            return DateDueState.TODAY
        }
        if (areCalendarEquals(GetTomorrowCalendarUseCase().invoke(), dateDue)) {
            return DateDueState.TOMORROW
        }
        return if (currentDate.after(dateDue)) {
            DateDueState.AFTER_DATE_DUE
        } else DateDueState.BEFORE_DATE_DUE
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