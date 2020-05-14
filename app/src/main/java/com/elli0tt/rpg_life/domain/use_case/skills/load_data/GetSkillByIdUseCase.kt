package com.elli0tt.rpg_life.domain.use_case.skills.load_data

import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.repository.SkillsRepository

class GetSkillByIdUseCase(private val skillsRepository: SkillsRepository) {
    fun invoke(skillId: Int): Skill {
        return skillsRepository.getSkillById(skillId)
    }
}