package com.elli0tt.rpg_life.domain.use_case.skills

import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState

class SortSkillsUseCase {
    operator fun invoke(skills: List<Skill>, sortingState: SkillsSortingState): List<Skill> {
        return when (sortingState) {
            SkillsSortingState.NAME -> sortByName(skills)
            SkillsSortingState.LEVEL -> sortByLevel(skills)
        }
    }

    private fun sortByName(skills: List<Skill>): List<Skill> {
        return skills.sortedWith(Comparator { skill1, skill2 ->
            skill1.name.compareTo(skill2.name)
        })
    }

    private fun sortByLevel(skills: List<Skill>): List<Skill> {
        return skills.sortedWith(Comparator { skill1, skill2 ->
            if (skill1.level != skill2.level) {
                skill1.level.compareTo(skill2.level)
            } else {
                skill1.name.compareTo(skill2.name)
            }
        })
    }
}