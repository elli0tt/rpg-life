package com.elli0tt.rpg_life.domain.use_case.skills.update_data

import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState

class SetSkillsSortingStateUseCase(private val skillsRepository: SkillsRepository) {
    fun invoke(skillsSortingState: SkillsSortingState) {
        skillsRepository.skillsSortingState = skillsSortingState
    }
}