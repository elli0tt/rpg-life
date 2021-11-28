package com.elli0tt.rpg_life.presentation.adapter.add_skill_to_quest

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.google.android.material.slider.Slider

class AddSkillsToQuestViewHolder(
    itemView: View,
    onXpPercentageSeekBarTouchStopListener: AddSkillsToQuestAdapter.OnXpPercentageSeekBarTouchStopListener
) : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val xpPercentageSlider: Slider = itemView.findViewById(R.id.xp_percentage_seek_bar)
    private val xpValueTextView: TextView = itemView.findViewById(R.id.xp_text_view)

    init {
        xpPercentageSlider.addOnChangeListener { slider, value, fromUser ->
            xpValueTextView.text =
                itemView.context.getString(R.string.add_skills_to_quest_xp_percent, value.toInt())
        }

        xpPercentageSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                //do nothing
            }

            override fun onStopTrackingTouch(slider: Slider) {
                onXpPercentageSeekBarTouchStopListener.onTouchStop(
                    adapterPosition,
                    slider.value.toInt()
                )
            }
        })
//        xpPercentageSlider.setOnsetOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                xpValueTextView.text = itemView.context.getString(R.string.add_skills_to_quest_xp_percent, progress)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                //do nothing
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                onXpPercentageSeekBarTouchStopListener.onTouchStop(adapterPosition, seekBar!!.progress)
//            }
//        })
    }

    fun bind(skill: AddSkillData) {
        nameTextView.text = skill.name
        xpPercentageSlider.value = skill.xpPercentage.toFloat()
        xpValueTextView.text = itemView.context.getString(
            R.string.add_skills_to_quest_xp_percent,
            xpPercentageSlider.value.toInt()
        )
    }
}