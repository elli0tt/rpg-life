package com.elli0tt.rpg_life.presentation.screen.add_category_to_skill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.SkillsCategory

class AddCategoryToSkillAdapter : ListAdapter<SkillsCategory, AddCategoryToSkillViewHolder>(DIFF_CALLBACK) {

    interface OnItemClickListener{
        fun onClick(position: Int)
    }

    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCategoryToSkillViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_category_to_skill_recycler_item, parent, false)
        return AddCategoryToSkillViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AddCategoryToSkillViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<SkillsCategory> = object : DiffUtil.ItemCallback<SkillsCategory>() {
            override fun areItemsTheSame(oldItem: SkillsCategory, newItem: SkillsCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SkillsCategory, newItem: SkillsCategory): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}