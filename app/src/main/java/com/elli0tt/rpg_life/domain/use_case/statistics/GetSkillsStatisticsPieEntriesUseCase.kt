package com.elli0tt.rpg_life.domain.use_case.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.skills.load_data.GetAllSkillsUseCase
import com.github.mikephil.charting.data.PieEntry

class GetSkillsStatisticsPieEntriesUseCase(skillsRepository: SkillsRepository) {
    private val getAllSkillsUseCase: GetAllSkillsUseCase = GetAllSkillsUseCase(skillsRepository)

    fun invoke(): LiveData<List<PieEntry>> {
        return Transformations.map(getAllSkillsUseCase.invoke()) { skills ->
            val result = ArrayList<PieEntry>(skills.size)

            val totalXpSum = skills.map { it.totalXp }.sum()

            for (skill in skills) {
                val value = skill.totalXp.toFloat() / totalXpSum * 100
                if (value > 0){
                    result.add(PieEntry(value, skill.name))
                }
            }
            result
        }
    }
}