package com.elli0tt.rpg_life.domain.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.elli0tt.rpg_life.domain.model.room_type_converters.CalendarConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.DifficultyConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.RelatedSkillsIdsConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.RepeatStateConverter;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "quest_table")
public class Quest {
    public enum Difficulty {
        VERY_EASY(200, 400, 1, 2),
        EASY(400, 800, 2, 4),
        NORMAL(1_000, 2_000, 3, 6),
        HARD(2_000, 4_000, 5, 10),
        VERY_HARD(10_000, 20_000, 10, 20),
        IMPOSSIBLE(30_000, 60_000, 30, 60),
        NOT_SET(0, 0, 0, 0);

        private int xpIncrease;
        private int xpDecrease;
        private int procrastinationIncrease;
        private int procrastinationDecrease;

        Difficulty(int xpIncrease, int xpDecrease, int procrastinationIncrease, int
                procrastinationDecrease) {
            this.xpIncrease = xpIncrease;
            this.xpDecrease = xpDecrease;
            this.procrastinationIncrease = procrastinationIncrease;
            this.procrastinationDecrease = procrastinationDecrease;
        }

        public int getXpIncrease() {
            return xpIncrease;
        }

        public int getXpDecrease() {
            return xpDecrease;
        }

        public int getProcrastinationIncrease() {
            return procrastinationIncrease;
        }

        public int getProcrastinationDecrease() {
            return procrastinationDecrease;
        }
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name = "";
    @NonNull
    private String description = "";

    @TypeConverters({DifficultyConverter.class})
    private Difficulty difficulty = Difficulty.NORMAL;

    private int parentQuestId;

    private boolean isSubQuest = false;

    @Ignore
    private List<Reward> rewards = new ArrayList<>();
    private boolean isImportant = false;
    private boolean isCompleted = false;

    @Ignore
    private Calendar startDate;
    @TypeConverters({CalendarConverter.class})
    private Calendar dateDue = Calendar.getInstance();

    /**
     * NOT_SET - dateDue wasn't set yet
     * AFTER_DATE_DUE - current date is after dateDue (deadline is expired)
     * BEFORE_DATE_DUE - current date is before dateDue (deadline isn't expired)
     */

    public enum DateDueState {
        NOT_SET, AFTER_DATE_DUE, BEFORE_DATE_DUE, TODAY, TOMORROW
    }

    private boolean isDateDueSet = false;

    //@TypeConverters({DateDueStateConverter.class})
    @Ignore
    private DateDueState dateDueState = DateDueState.NOT_SET;

    public enum RepeatState {
        DAILY, WEEKDAYS, WEEKENDS, WEEKLY, MONTHLY, YEARLY, CUSTOM, NOT_SET
    }

    @TypeConverters({RepeatStateConverter.class})
    private RepeatState repeatState = RepeatState.NOT_SET;

    @TypeConverters({RelatedSkillsIdsConverter.class})
    private List<Integer> relatedSkillsIds = new ArrayList<>();

    @Ignore
    public Quest(@NonNull String name,
                 @NonNull String description,
                 Difficulty difficulty,
                 List<Reward> rewards,
                 boolean isImportant) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.rewards = rewards;
        this.isImportant = isImportant;
    }

    public Quest() {
        // do nothing
    }

    @Ignore
    public Quest(@NonNull String name) {
        this.name = name;
    }

    @Ignore
    public Quest(@NonNull String name, @NonNull String description, Difficulty difficulty,
                 Calendar dateDue, DateDueState dateDueState) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.dateDue = dateDue;
        this.dateDueState = dateDueState;
    }

    @Ignore
    public Quest(int id, @NonNull String name, @NonNull String description,
                 Difficulty difficulty, Calendar dateDue, boolean isCompleted,
                 boolean isImportant
                 //DateDueState dateDueState
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.dateDue = dateDue;
        this.isCompleted = isCompleted;
        this.isImportant = isImportant;
        //this.dateDueState = dateDueState;
    }


    private static final int COMPLETED_PERCENTAGE = 100;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getParentQuestId() {
        return parentQuestId;
    }

    public boolean isSubQuest() {
        return isSubQuest;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public Calendar getDateDue() {
        return dateDue;
    }

    public DateDueState getDateDueState() {
        if (!isDateDueSet) {
            return DateDueState.NOT_SET;
        }
        Calendar currentDate = Calendar.getInstance();
        if (areCalendarEquals(new GetTodayCalendarUseCase().invoke(), getDateDue())) {
            return Quest.DateDueState.TODAY;
        }
        if (areCalendarEquals(new GetTomorrowCalendarUseCase().invoke(), getDateDue())) {
            return Quest.DateDueState.TOMORROW;
        }
        if (currentDate.after(dateDue)) {
            return Quest.DateDueState.AFTER_DATE_DUE;
        }
        return Quest.DateDueState.BEFORE_DATE_DUE;
    }

    private boolean areCalendarEquals(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH) &&
                calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY) &&
                calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE);
    }

    public boolean isDateDueSet() {
        return isDateDueSet;
    }

    public RepeatState getRepeatState() {
        return repeatState;
    }

    public List<Integer> getRelatedSkillsIds() {
        return relatedSkillsIds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setParentQuestId(int parentQuestId) {
        this.parentQuestId = parentQuestId;
    }

    public void setIsSubQuest(boolean isSubQuest) {
        this.isSubQuest = isSubQuest;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public void setDateDue(Calendar dateDue) {
        this.dateDue = dateDue;
    }

//    public void setDateDueState(DateDueState dateDueState){
//        this.dateDueState = dateDueState;
//    }

    public void setIsDateDueSet(boolean isDateDueSet) {
        this.isDateDueSet = isDateDueSet;
    }

    public void setRepeatState(RepeatState repeatState) {
        this.repeatState = repeatState;
    }

    public void setRelatedSkillsIds(List<Integer> relatedSkillsIds) {
        this.relatedSkillsIds = relatedSkillsIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "name: " + name;
    }

    public static String getDateDueFormatted(Calendar dateDue) {
        return String.format(Locale.getDefault(), "%02d.%02d.%04d %02d:%02d",
                dateDue.get(Calendar.DAY_OF_MONTH),
                dateDue.get(Calendar.MONTH) + 1,
                dateDue.get(Calendar.YEAR),
                dateDue.get(Calendar.HOUR_OF_DAY),
                dateDue.get(Calendar.MINUTE));
    }
}
