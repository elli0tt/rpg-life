package com.elli0tt.rpg_life.data.database.room_database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.elli0tt.rpg_life.data.dao.CharacteristicsDao;
import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.dao.RelatedToQuestsSkillsDao;
import com.elli0tt.rpg_life.data.dao.SkillsCategoriesDao;
import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.domain.model.Characteristic;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.model.SkillsCategory;
import com.elli0tt.rpg_life.domain.model.room_type_converters.CalendarConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.DateStateConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.DifficultyConverter;
import com.elli0tt.rpg_life.domain.model.room_type_converters.RepeatStateConverter;

@Database(entities = {Characteristic.class, Quest.class, Skill.class, RelatedToQuestSkills.class,
        SkillsCategory.class}
        , version = 8, exportSchema = true)
@TypeConverters({DifficultyConverter.class, CalendarConverter.class, DateStateConverter.class,
        RepeatStateConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract CharacteristicsDao getCharacteristicsDao();

    public abstract QuestsDao getQuestDao();

    public abstract SkillsDao getSkillsDao();

    public abstract RelatedToQuestsSkillsDao getRelatedToQuestSkillsDao();

    public abstract SkillsCategoriesDao getSkillsCategoriesDao();

    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "app_database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,
                                    MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8)
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table quest_table add column isChallenge INTEGER default 0 " +
                    "not null");
            database.execSQL("alter table quest_table add column totalDaysCount INTEGER default 0" +
                    " not null");
            database.execSQL("alter table quest_table add column dayNumber INTEGER default 0 not " +
                    "null");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table related_to_quests_skills add column xpPercentage " +
                    "integer default 100 not null");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `skills_categories_table` (`id` INTEGER " +
                    "PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE skills_table ADD COLUMN categoryId INTEGER DEFAULT 0 " +
                    "NOT NULL");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE quest_table ADD COLUMN startDate INTEGER");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE quest_table ADD COLUMN isStartDateSet INTEGER DEFAULT 0" +
                    " NOT NULL");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Create a new translation table
            database.execSQL("CREATE TABLE IF NOT EXISTS `quest_table_new` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " `name` TEXT NOT NULL," +
                    " `description` TEXT NOT NULL," +
                    " `difficulty` INTEGER NOT NULL," +
                    " `parentQuestId` INTEGER NOT NULL," +
                    " `isSubQuest` INTEGER NOT NULL," +
                    " `isImportant` INTEGER NOT NULL," +
                    " `isCompleted` INTEGER NOT NULL," +
                    " `startDate` INTEGER NOT NULL," +
                    " `dateDue` INTEGER NOT NULL," +
                    " `startDateState` INTEGER NOT NULL," +
                    " `dateDueState` INTEGER NOT NULL," +
                    " `repeatState` INTEGER NOT NULL," +
                    " `hasSubquests` INTEGER NOT NULL," +
                    " `isChallenge` INTEGER NOT NULL," +
                    " `totalDaysCount` INTEGER NOT NULL," +
                    " `dayNumber` INTEGER NOT NULL)");
            // Copy the data
            database.execSQL("INSERT INTO `quest_table_new` (id, name, description, difficulty, " +
                    "parentQuestId, isSubQuest, isImportant, isCompleted, startDate, dateDue, " +
                    "startDateState, dateDueState, repeatState, hasSubquests, isChallenge, " +
                    "totalDaysCount, dayNumber) " +
                    "SELECT id, name, description, difficulty, parentQuestId, isSubQuest, " +
                    "isImportant, isCompleted, 0, dateDue, 0, 0, repeatState, hasSubquests, " +
                    "isChallenge, " +
                    "totalDaysCount, dayNumber " +
                    "FROM quest_table");
            // Remove old table
            database.execSQL("DROP TABLE quest_table");
            // Change name of table to correct one
            database.execSQL("ALTER TABLE quest_table_new RENAME TO quest_table");
        }
    };

    /**
     * Drop old table and add new with necessary column example
     */
//    public static final Migration MIGRATION_4_5 = new Migration(4, 5){
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            // Create a new translation table
//            database.execSQL("CREATE TABLE IF NOT EXISTS `skills_table_new`" +
//                    " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                    " `name` TEXT NOT NULL," +
//                    " `timeSpentMillis` INTEGER NOT NULL," +
//                    " `totalXp` INTEGER NOT NULL," +
//                    " `categoryId` INTEGER NOT NULL," +
//                    " FOREIGN KEY(`categoryId`) REFERENCES `skills_categories_table`(`id`) ON
//                    UPDATE CASCADE ON DELETE NO ACTION )");
//            // Copy the data
//            database.execSQL("INSERT INTO `skills_table_new` (id, name, timeSpentMillis,
//            totalXp, categoryId) " +
//                    "SELECT id, name, timeSpentMillis, totalXp, 0 " +
//                    "FROM skills_table");
//            // Remove old table
//            database.execSQL("DROP TABLE skills_table");
//            // Change name of table to correct one
//            database.execSQL("ALTER TABLE skills_table_new RENAME TO skills_table");
//        }
//    };

}
