package com.elli0tt.rpg_life.presentation.screen.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment
import com.elli0tt.rpg_life.presentation.extensions.injectViewModel
import com.elli0tt.rpg_life.presentation.screen.statistics.di.StatisticsComponent
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import javax.inject.Inject

class StatisticsFragment : BaseFragment(R.layout.fragment_statistics) {

    private lateinit var statisticsComponent: StatisticsComponent

    private lateinit var viewModel: StatisticsViewModel

    private lateinit var skillsPieChart: PieChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDagger()

        skillsPieChart = view.findViewById(R.id.skills_pie_chart)
        initSkillsPieChart()

        subscribeToViewModel()
        setHasOptionsMenu(true)
    }

    private fun initDagger() {
        statisticsComponent = appComponent.statisticsComponentFactory().create()
        statisticsComponent.inject(this)

        viewModel = injectViewModel(viewModelFactory)
    }

    private fun subscribeToViewModel() {
        viewModel.pieEntries.observe(viewLifecycleOwner) { pieEntries ->
            val dataSet = PieDataSet(pieEntries, "").apply {
                colors = viewModel.colors
                sliceSpace = 2f
                valueLinePart1OffsetPercentage = 90f
                valueLinePart1Length = 0.6f
                valueLinePart2Length = 0.7f
//                yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            }
            val pieData = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(skillsPieChart))
                setValueTextSize(15f)
                setValueTextColor(Color.BLACK)
            }
            skillsPieChart.data = pieData
            skillsPieChart.animateY(1400, Easing.EaseInOutQuad)
            //skillsPieChart.invalidate()
        }
    }

    private fun initSkillsPieChart() {
        skillsPieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
//            setExtraOffsets(30f, 0f, 30f, 0f)
            animateY(1400, Easing.EaseInOutQuad)
            isDrawHoleEnabled = false
            setDrawEntryLabels(false)
//            setEntryLabelTextSize(20f)
//            setEntryLabelColor(Color.BLACK)
            minAngleForSlices = 2f

            legend.apply {
                isWordWrapEnabled = true
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                xEntrySpace = 7f
                form = Legend.LegendForm.CIRCLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.statistics_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.skills -> viewModel.showSkillsStatistics()
            R.id.skills_categories -> viewModel.showSkillsCategoriesStatistics()
        }
        return super.onOptionsItemSelected(item)
    }

}