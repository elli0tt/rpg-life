package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.AddSkillData

class AddSkillsToQuestViewHolder(itemView: View,
                                 onSelectCheckBoxClickListener: AddSkillsToQuestAdapter.OnSelectCheckBoxClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private val nameTextView: TextView
    private val selectCheckBox: CheckBox

    init {
        nameTextView = itemView.findViewById(R.id.add_skills_to_quest_recycler_item_name_text_view)
        selectCheckBox = itemView.findViewById<CheckBox>(R.id.add_skills_to_quest_recycler_item_select_check_box)

        selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onSelectCheckBoxClickListener.onCheck(adapterPosition, isChecked)
        }
    }

    fun bind(skill: AddSkillData) {
        nameTextView.text = skill.name
        selectCheckBox.isChecked = skill.isSelected
    }


}