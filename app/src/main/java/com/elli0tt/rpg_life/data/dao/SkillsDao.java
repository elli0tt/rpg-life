package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.elli0tt.rpg_life.domain.model.AddSkillData;
import com.elli0tt.rpg_life.domain.model.Skill;

import java.util.List;

@Dao
public interface SkillsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Skill... skills);

    @Query("SELECT * FROM skills_table ORDER BY id")
    LiveData<List<Skill>> getAllSkills();

    @Query("SELECT name FROM skills_table WHERE id IN (:ids) ORDER BY NAME")
    List<String> getSkillsNamesByIds(List<Integer> ids);


    @Query("DELETE FROM skills_table")
    void deleteAll();
}
