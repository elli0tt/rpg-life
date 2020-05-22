package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.elli0tt.rpg_life.domain.model.Skill;

import java.util.List;

@Dao
public interface SkillsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSkill(Skill... skills);

    @Query("SELECT * FROM skills_table ORDER BY id")
    LiveData<List<Skill>> getAllSkills();

    @Query("SELECT name FROM skills_table WHERE id IN (:ids) ORDER BY NAME")
    List<String> getSkillsNamesByIds(List<Integer> ids);

    @Query("SELECT * FROM skills_table WHERE id = :skillId")
    Skill getSkillById(int skillId);

    @Update
    void updateSkill(Skill... skills);

    @Query("update skills_table set totalXp = totalXp + :xpIncrease where id = :id")
    void updateSkillTotalXpById(int id, long xpIncrease);

    @Query("DELETE FROM skills_table")
    void deleteAllSkills();

    @Query("UPDATE skills_table SET categoryId = :categoryId WHERE id = :skillId")
    void updateSkillCategoryById(int skillId, int categoryId);
}
