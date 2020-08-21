package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.SkillsCategory

class AddCategoryToSkillViewHolder(itemView: View,
                                   onItemClickListener: AddCategoryToSkillAdapter.OnItemClickListener)
    : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClickListener.onClick(adapterPosition)
            }
        }
    }

    fun bind(skillsCategory: SkillsCategory) {
        nameTextView.text = skillsCategory.name
    }
}