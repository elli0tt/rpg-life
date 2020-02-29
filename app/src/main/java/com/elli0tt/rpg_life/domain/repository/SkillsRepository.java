package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Skill;

import java.util.List;

public interface SkillsRepository {
    void insert(Skill... skills);

    LiveData<List<Skill>> getAllSkills();

    void deleteAll();
}
