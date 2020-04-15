package com.elli0tt.rpg_life.domain.use_case.skills.update_data;

import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

public class UpdateSkillTotalXpByIdUseCase {
    private SkillsRepository skillsRepository;

    public UpdateSkillTotalXpByIdUseCase(SkillsRepository skillsRepository){
        this.skillsRepository = skillsRepository;
    }

    public void invoke(int id, long totalXp){
        skillsRepository.updateTotalXpById(id, totalXp);
    }
}
