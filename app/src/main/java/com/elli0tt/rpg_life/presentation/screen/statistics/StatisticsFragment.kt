package com.elli0tt.rpg_life.presentation.screen.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.elli0tt.rpg_life.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter


class StatisticsFragment : Fragment() {
    private lateinit var viewModel: StatisticsViewModel

    private lateinit var skillsPieChart: PieChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)

        skillsPieChart = view.findViewById(R.id.skills_pie_chart)

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

        subscribeToViewModel()
        setHasOptionsMenu(true)
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