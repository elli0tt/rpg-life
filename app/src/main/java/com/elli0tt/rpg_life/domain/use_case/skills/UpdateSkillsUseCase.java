package com.elli0tt.rpg_life.domain.use_case.skills;

import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

public class UpdateSkillsUseCase {
    private SkillsRepository skillsRepository;

    public UpdateSkillsUseCase(SkillsRepository skillsRepository){
        this.skillsRepository = skillsRepository;
    }

    public void invoke(Skill... skills){
        skillsRepository.update(skills);
    }
}
