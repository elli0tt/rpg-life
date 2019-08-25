package com.elli0tt.rpg_life.domain.modal;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "quest_table")
public class Quest {
//    private enum Difficulty {
//        VERY_EASY(200, 400, 1, 2),
//        EASY(400, 800, 2, 4),
//        NORMAL(1_000, 2_000, 3, 6),
//        HARD(2_000, 4_000, 5, 10),
//        VERY_HARD(10_000, 20_000, 10, 20),
//        IMPOSSIBLE(30_000, 60_000, 30, 60);
//
//        private int xpIncrease;
//        private int xpDecrease;
//        private int procrastinationIncrease;
//        private int procrastinationDecrease;
//
//        Difficulty(int xpIncrease, int xpDecrease, int procrastinationIncrease, int procrastinationDecrease) {
//            this.xpIncrease = xpIncrease;
//            this.xpDecrease = xpDecrease;
//            this.procrastinationIncrease = procrastinationIncrease;
//            this.procrastinationDecrease = procrastinationDecrease;
//        }
//
//        public int getXpIncrease() {
//            return xpIncrease;
//        }
//
//        public int getXpDecrease() {
//            return xpDecrease;
//        }
//
//        public int getProcrastinationIncrease() {
//            return procrastinationIncrease;
//        }
//
//        public int getProcrastinationDecrease() {
//            return procrastinationDecrease;
//        }
//    }

    @IntDef({VERY_EASY, EASY, NORMAL, HARD, VERY_HARD, IMPOSSIBLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Difficulty {
        //Do nothing
    }

    //difficulty levels constants
    public static final int VERY_EASY = 0;
    public static final int EASY = 1;
    public static final int NORMAL = 2;
    public static final int HARD = 3;
    public static final int VERY_HARD = 4;
    public static final int IMPOSSIBLE = 5;

    @PrimaryKey(autoGenerate = true) private int id;

    @NonNull private String name = "";
    @NonNull private String description = "";
    @Difficulty private int difficulty = NORMAL;
    @Ignore private List<Quest> subquests = new ArrayList<>();
    @Ignore private List<Reward> rewards = new ArrayList<>();
    private boolean isImportant = false;
    private boolean isCompleted = false;

    // TODO set default values for start and finish dates
    @Ignore private Calendar startDate;
    @Ignore private Calendar finishDate;

    @Ignore
    public Quest(@NonNull String name,
                 String description,
                 @Difficulty int difficulty,
                 List<Quest> subquests,
                 List<Reward> rewards,
                 boolean isImportant) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.subquests = subquests;
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
    public Quest(@NonNull String name, @NonNull String description, @Difficulty int difficulty) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
    }

    @Ignore
    public Quest(int id, @NonNull String name, @NonNull String description, @Difficulty int difficulty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
    }



    private static final int COMPLETED_PERCENTAGE = 100;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {
        for (Quest quest : subquests) {
            quest.complete();
        }
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

    public @Difficulty
    int getDifficulty() {
        return difficulty;
    }

    public List<Quest> getSubquests() {
        return subquests;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public boolean isImportant() {
        return isImportant;
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

    public void setDifficulty(@Difficulty int difficulty) {
        this.difficulty = difficulty;
    }

    public void setSubquests(List<Quest> subquests) {
        this.subquests = subquests;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public int getIncreaseXp() {
        switch (difficulty) {
            case VERY_EASY:
                return 200;
            case EASY:
                return 400;
            case NORMAL:
                return 1_000;
            case HARD:
                return 2_000;
            case VERY_HARD:
                return 10_000;
            case IMPOSSIBLE:
                return 30_000;
            default:
                return -1;
        }
    }

    public static boolean isNameValid(String name){
        return !name.trim().isEmpty();
    }
}
