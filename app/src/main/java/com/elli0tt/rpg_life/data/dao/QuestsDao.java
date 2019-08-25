package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.List;

@Dao
public interface QuestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quest quest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Quest> questList);

    @Query("SELECT * FROM quest_table WHERE id = :questId")
    Quest getQuestById(int questId);

    @Query("SELECT * FROM quest_table ORDER BY name ")
    LiveData<List<Quest>> getAllQuests();

    @Update
    void update(Quest quest);

    @Query("DELETE FROM quest_table")
    void deleteAll();
}
