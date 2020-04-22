package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.view.View
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.AddSkillData

class AddSkillsToQuestViewHolder(itemView: View,
                                 onSelectCheckBoxClickListener: AddSkillsToQuestAdapter.OnSelectCheckBoxClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
    private val selectCheckBox: CheckBox = itemView.findViewById(R.id.select_check_box)
    private val xpPercentageSeekBar: SeekBar = itemView.findViewById(R.id.xp_percentage_seek_bar)
    private val xpValueTextView: TextView = itemView.findViewById(R.id.xp_text_view)

    init {
        selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onSelectCheckBoxClickListener.onCheck(adapterPosition, isChecked, xpPercentageSeekBar.progress)
        }
        xpPercentageSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                xpValueTextView.text = itemView.context.getString(R.string.add_skills_to_quest_xp, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }
        })
    }

    fun bind(skill: AddSkillData) {
        nameTextView.text = skill.name
        xpPercentageSeekBar.progress = skill.xpPercentage
        selectCheckBox.isChecked = skill.isSelected
    }


}