package com.elli0tt.rpg_life.data.database.room_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.elli0tt.rpg_life.data.dao.CharacteristicsDao;
import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.domain.modal.Characteristic;
import com.elli0tt.rpg_life.domain.modal.Quest;

@Database(entities = {Characteristic.class, Quest.class}, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract CharacteristicsDao getCharacteristicsDao();

    public abstract QuestsDao getQuestDao();

    private static volatile AppRoomDatabase INSTANCE;

    public static AppRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
