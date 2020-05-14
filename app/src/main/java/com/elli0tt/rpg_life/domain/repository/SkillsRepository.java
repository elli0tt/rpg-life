package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState;

import java.util.List;

public interface SkillsRepository {
    void insert(Skill... skills);

    LiveData<List<Skill>> getAllSkills();

    List<String> getSkillsNamesByIds(List<Integer> ids);

    void update(Skill... skills);

    void updateTotalXpById(int id, long xpIncrease);

    void deleteAll();

    SkillsSortingState getSkillsSortingState();

    void setSkillsSortingState(SkillsSortingState sortingState);

    Skill getSkillById(int skillId);
}
