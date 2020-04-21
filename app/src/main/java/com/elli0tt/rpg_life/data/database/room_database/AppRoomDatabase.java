package com.elli0tt.rpg_life.data.database.room_database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.elli0tt.rpg_life.data.dao.CharacteristicsDao;
import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.dao.RelatedToQuestsSkillsDao;
import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.domain.model.Characteristic;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.domain.model.Skill;

@Database(entities = {Characteristic.class, Quest.class, Skill.class, RelatedToQuestSkills.class}, version = 2)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract CharacteristicsDao getCharacteristicsDao();
    public abstract QuestsDao getQuestDao();
    public abstract SkillsDao getSkillsDao();
    public abstract RelatedToQuestsSkillsDao getRelatedToQuestSkillsDao();

    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "app_database")
                            .addMigrations(MIGRAGTION_1_2)
                            //.fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static final Migration MIGRAGTION_1_2 = new Migration(1, 2){
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table quest_table add column isChallenge INTEGER default 0 not null");
            database.execSQL("alter table quest_table add column totalDaysCount INTEGER default 0 not null");
            database.execSQL("alter table quest_table add column dayNumber INTEGER default 0 not null");
        }
    };

}
