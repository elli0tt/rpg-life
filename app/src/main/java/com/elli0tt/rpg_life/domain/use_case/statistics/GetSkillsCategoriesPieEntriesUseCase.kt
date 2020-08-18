package com.elli0tt.rpg_life.domain.use_case.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.elli0tt.rpg_life.domain.model.Skill
import com.elli0tt.rpg_life.domain.model.SkillsCategory
import com.elli0tt.rpg_life.domain.repository.SkillsCategoriesRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.github.mikephil.charting.data.PieEntry

class GetSkillsCategoriesPieEntriesUseCase(skillsRepository: SkillsRepository,
                                           skillsCategoriesRepository: SkillsCategoriesRepository) {
    private val resultPieEntries: MediatorLiveData<List<PieEntry>> = MediatorLiveData()
    private val skills = skillsRepository.allSkills
    private val skillsCategories = skillsCategoriesRepository.getAllSkillsCategories()

    fun invoke(): LiveData<List<PieEntry>> {
        resultPieEntries.addSource(skills) { skills ->
            resultPieEntries.value = mapToPieEntries(skills, skillsCategories.value)
        }

        resultPieEntries.addSource(skillsCategories) { skillsCategories ->
            resultPieEntries.value = mapToPieEntries(skills.value, skillsCategories)
        }

        return resultPieEntries
    }

    private fun mapToPieEntries(skills: List<Skill>?, skillsCategories: List<SkillsCategory>?): List<PieEntry> {
        val result = ArrayList<PieEntry>()
        if (skills != null && skillsCategories != null) {
            val totalXpSum = skills.map { it.totalXp }.sum()
            for (category in skillsCategories) {
                val categoryXp = skills
                        .filter { it.categoryId == category.id }
                        .map { it.totalXp }
                        .sum()
                result.add(PieEntry(categoryXp.toFloat() / totalXpSum * 100, category.name))
            }

            for (skill in skills) {
                val value = skill.totalXp.toFloat() / totalXpSum * 100
                if (skill.categoryId == 0 && value > 0) {
                    result.add(PieEntry(value, skill.name))
                }
            }
        }
        return result
    }
}