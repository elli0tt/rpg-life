package com.elli0tt.rpg_life.presentation.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.elli0tt.rpg_life.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate


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
            setExtraOffsets(5f, 10f, 5f, 5f)
            animateY(1400, Easing.EaseInOutQuad)
            isDrawHoleEnabled = false
            setEntryLabelTextSize(20f)
            setEntryLabelColor(Color.BLACK)
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        viewModel.skillsPieEntries.observe(viewLifecycleOwner) { pieEntries ->
            val dataSet = PieDataSet(pieEntries, getString(R.string.skills_statistics_pie_chart_label)).apply {
                colors = viewModel.colors
                sliceSpace = 3f
            }
            val pieData = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(skillsPieChart))
                setValueTextSize(15f)

            }
            skillsPieChart.data = pieData
            skillsPieChart.invalidate()
        }
    }

}