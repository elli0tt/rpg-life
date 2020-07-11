package com.elli0tt.rpg_life.presentation.screen.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.SkillsCategoriesRepositoryImpl
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.use_case.statistics.GetSkillsCategoriesPieEntriesUseCase
import com.elli0tt.rpg_life.domain.use_case.statistics.GetSkillsPieEntriesUseCase
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val skillsRepository = SkillsRepositoryImpl(application)
    private val skillsCategoriesRepository = SkillsCategoriesRepositoryImpl(application)

    private val getSkillsPieEntriesUseCase: GetSkillsPieEntriesUseCase = GetSkillsPieEntriesUseCase(skillsRepository)
    private val getSkillsCategoriesPieEntriesUseCase: GetSkillsCategoriesPieEntriesUseCase = GetSkillsCategoriesPieEntriesUseCase(skillsRepository, skillsCategoriesRepository)

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