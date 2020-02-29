package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.List;

@Dao
public interface QuestsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quest... quests);

    @Query("SELECT * FROM quest_table WHERE id = :questId")
    Quest getQuestById(int questId);

    @Query("SELECT * FROM quest_table WHERE id IN (:ids) ORDER BY name")
    List<Quest> getQuestsById(List<Integer> ids);

    @Query("SELECT * FROM quest_table WHERE isSubQuest = 0 ORDER BY name")
    LiveData<List<Quest>> getAllQuests();

    @Query("SELECT * FROM quest_table WHERE isCompleted = 0 AND isSubQuest = 0 ORDER BY name")
    LiveData<List<Quest>> getActiveQuests();

    @Query("SELECT * FROM quest_table WHERE isCompleted = 1 AND isSubQuest = 0 ORDER BY name")
    LiveData<List<Quest>> getCompletedQuests();

    @Query("SELECT * FROM quest_table WHERE isImportant = 1 AND isCompleted = 0 AND isSubQuest = 0 ORDER BY name")
    LiveData<List<Quest>> getImportantQuests();

    @Query("SELECT * FROM quest_table WHERE parentQuestId = :parentQuestId ORDER BY id")
    LiveData<List<Quest>> getSubQuests(int parentQuestId);

    @Update
    void update(Quest... quests);

    @Delete
    void delete(Quest... quests);

    @Query("DELETE FROM quest_table")
    void deleteAll();
}
