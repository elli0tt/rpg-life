package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState;

import java.util.List;

public interface SkillsRepository {
    void insertSkills(Skill... skills);

    LiveData<List<Skill>> getAllSkills();

    List<String> getSkillsNamesByIds(List<Integer> ids);

    void updateSkills(Skill... skills);

    void updateSkillTotalXpById(int id, long xpIncrease);

    void deleteAllSkills();

    SkillsSortingState getSkillsSortingState();

    void setSkillsSortingState(SkillsSortingState sortingState);

    Skill getSkillById(int skillId);

    void updateSkillCategoryById(int skillId, int categoryId);

    void deleteSkillsById(Integer... skillId);
}
