package com.elli0tt.rpg_life.domain.use_case.skills.load_data

import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState

class GetSkillsSortingStateUseCase(private val skillsRepository: SkillsRepository) {
    fun invoke(): SkillsSortingState{
        return skillsRepository.skillsSortingState
    }
}