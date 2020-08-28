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
    List<Long> insertQuests(Quest... quests);

    @Query("SELECT * FROM quest_table WHERE id = :questId")
    Quest getQuestById(int questId);

    @Query("SELECT * FROM quest_table WHERE id IN (:ids) ORDER BY name")
    List<Quest> getQuestsById(List<Integer> ids);

    @Query("SELECT * FROM quest_table WHERE isSubQuest = 0 AND isChallenge = 0")
    LiveData<List<Quest>> getAllQuests();

    @Query("SELECT * FROM quest_table WHERE parentQuestId = :parentQuestId ORDER BY id")
    LiveData<List<Quest>> getSubQuests(int parentQuestId);

    @Query("select * from quest_table where id = :questId")
    LiveData<Quest> getQuestByIdLiveData(int questId);

    @Update
    void updateQuests(Quest... quests);

    @Query("update quest_table set hasSubquests = :hasSubquest where id = :id")
    void updateHasSubquestsById(int id, boolean hasSubquest);

    @Delete
    void deleteQuests(Quest... quests);

    @Query("DELETE FROM quest_table")
    void deleteAllQuests();
}
