package com.elli0tt.rpg_life.presentation.screen.statistics

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.use_case.statistics.GetSkillsCategoriesPieEntriesUseCase
import com.elli0tt.rpg_life.domain.use_case.statistics.GetSkillsPieEntriesUseCase
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
        getSkillsPieEntriesUseCase: GetSkillsPieEntriesUseCase,
        getSkillsCategoriesPieEntriesUseCase: GetSkillsCategoriesPieEntriesUseCase
) : ViewModel() {
    private val skillsPieEntries = getSkillsPieEntriesUseCase.invoke()
    private val skillsCategoriesPieEntries = getSkillsCategoriesPieEntriesUseCase.invoke()

    enum class StatisticsMode {
        SKILLS, SKILLS_CATEGORIES
    }

    private val statisticsMode: MutableLiveData<StatisticsMode> = MutableLiveData(StatisticsMode.SKILLS)

    var pieEntries: MediatorLiveData<List<PieEntry>> = MediatorLiveData()

    val colors: ArrayList<Int> = ArrayList()

    init {
        initColors()
        pieEntries.addSource(skillsPieEntries) { skillsPieEntries ->
            if (statisticsMode.value?.equals(StatisticsMode.SKILLS) == true) {
                pieEntries.value = skillsPieEntries
            }
        }

        pieEntries.addSource(skillsCategoriesPieEntries) { skillsCategoriesPieEntries ->
            if (statisticsMode.value?.equals(StatisticsMode.SKILLS_CATEGORIES) == true) {
                pieEntries.value = skillsCategoriesPieEntries
            }
        }

        pieEntries.addSource(statisticsMode) { statisticsMode ->
            when (statisticsMode) {
                StatisticsMode.SKILLS -> pieEntries.value = skillsPieEntries.value
                StatisticsMode.SKILLS_CATEGORIES -> pieEntries.value = skillsCategoriesPieEntries.value
                else -> { // do nothing
                }
            }
        }
    }

    private fun initColors() {
        for (c: Int in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c)
        }
        for (c: Int in ColorTemplate.JOYFUL_COLORS) {
            colors.add(c)
        }
        for (c: Int in ColorTemplate.COLORFUL_COLORS) {
            colors.add(c)
        }
        for (c: Int in ColorTemplate.LIBERTY_COLORS) {
            colors.add(c)
        }
        for (c: Int in ColorTemplate.PASTEL_COLORS) {
            colors.add(c)
        }
    }

    fun showSkillsStatistics() {
        statisticsMode.value = StatisticsMode.SKILLS
    }

    fun showSkillsCategoriesStatistics() {
        statisticsMode.value = StatisticsMode.SKILLS_CATEGORIES
    }
}