package com.elli0tt.rpg_life.domain.use_case.skills.load_data;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

import java.util.List;

public class GetAllSkillsUseCase {
    private SkillsRepository repository;

    public GetAllSkillsUseCase(SkillsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Skill>> invoke(){
        return repository.getAllSkills();
    }
}
