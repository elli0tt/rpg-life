package com.elli0tt.rpg_life.presentation.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.domain.use_case.statistics.GetSkillsStatisticsPieEntriesUseCase
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val skillsRepository = SkillsRepositoryImpl(application)

    private val getSkillsStatisticsPieEntriesUseCase: GetSkillsStatisticsPieEntriesUseCase
            = GetSkillsStatisticsPieEntriesUseCase(skillsRepository)

    val skillsPieEntries: LiveData<List<PieEntry>> = getSkillsStatisticsPieEntriesUseCase.invoke()

    val colors: ArrayList<Int> = ArrayList()

    init {
        initColors()
    }

    private fun initColors(){
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

    private val skillsPieData: LiveData<PieData> = Transformations.map(getSkillsStatisticsPieEntriesUseCase.invoke()) { pieEntries ->


        val result = PieData().apply {

        }

        result
    }
}