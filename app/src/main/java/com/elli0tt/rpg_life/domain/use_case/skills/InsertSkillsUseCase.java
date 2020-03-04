package com.elli0tt.rpg_life.domain.use_case.skills;

import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

public class InsertSkillsUseCase {
    private SkillsRepository repository;

    public InsertSkillsUseCase(SkillsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Skill... skills){
        repository.insert(skills);
    }
}
