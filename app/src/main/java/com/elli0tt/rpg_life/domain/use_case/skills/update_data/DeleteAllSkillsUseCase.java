package com.elli0tt.rpg_life.domain.use_case.skills.update_data;

import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

public class DeleteAllSkillsUseCase {
    private SkillsRepository repository;

    public DeleteAllSkillsUseCase(SkillsRepository repository) {
        this.repository = repository;
    }

    public void invoke(){
        repository.deleteAll();
    }
}
