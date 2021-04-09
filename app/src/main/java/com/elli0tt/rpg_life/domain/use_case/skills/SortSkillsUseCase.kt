package com.elli0tt.rpg_life.domain.use_case.skills

import com.elli0tt.rpg_life.domain.model.Skill
import javax.inject.Inject

class SortSkillsUseCase @Inject constructor() {
    operator fun invoke(skills: List<Skill>, sortingState: SkillsSortingState): List<Skill> {
        return when (sortingState) {
            SkillsSortingState.NAME_ASC -> sortByNameAsc(skills)
            SkillsSortingState.NAME_DESC -> sortByNameDesc(skills)
            SkillsSortingState.LEVEL_ASC -> sortByLevelAsc(skills)
            SkillsSortingState.LEVEL_DESC -> sortByLevelDesc(skills)
        }
    }

    private fun sortByNameAsc(skills: List<Skill>): List<Skill> {
        return skills.sortedWith { skill1, skill2 ->
            skill1.name.compareTo(skill2.name)
        }
    }

    private fun sortByNameDesc(skills: List<Skill>): List<Skill> {
        return skills.sortedWith { skill1, skill2 ->
            skill2.name.compareTo(skill1.name)
        }
    }

    private fun sortByLevelAsc(skills: List<Skill>): List<Skill> {
        return skills.sortedWith { skill1, skill2 ->
            when {
                skill1.level != skill2.level -> {
                    skill1.level.compareTo(skill2.level)
                }
                skill1.xpLeftToNextLevel != skill2.xpLeftToNextLevel -> {
                    skill2.xpLeftToNextLevel.compareTo(skill1.xpLeftToNextLevel)
                }
                else -> {
                    skill1.name.compareTo(skill2.name)
                }
            }
        }
    }

    private fun sortByLevelDesc(skills: List<Skill>): List<Skill> {
        return skills.sortedWith { skill1, skill2 ->
            when {
                skill1.level != skill2.level -> {
                    skill2.level.compareTo(skill1.level)
                }
                skill1.xpLeftToNextLevel != skill2.xpLeftToNextLevel -> {
                    skill1.xpLeftToNextLevel.compareTo(skill2.xpLeftToNextLevel)
                }
                else -> {
                    skill1.name.compareTo(skill2.name)
                }
            }
        }
    }
}