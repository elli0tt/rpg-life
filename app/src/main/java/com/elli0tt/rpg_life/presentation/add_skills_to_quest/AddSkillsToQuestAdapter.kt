package com.elli0tt.rpg_life.presentation.add_skills_to_quest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.AddSkillData
import com.elli0tt.rpg_life.domain.model.Quest
import java.util.*
import kotlin.collections.ArrayList

class AddSkillsToQuestAdapter : ListAdapter<AddSkillData, AddSkillsToQuestViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSkillsToQuestViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_skills_to_quest_recycler_item, parent, false)
        return AddSkillsToQuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddSkillsToQuestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<AddSkillData> = object : DiffUtil.ItemCallback<AddSkillData>() {
            override fun areItemsTheSame(oldItem: AddSkillData, newItem: AddSkillData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AddSkillData, newItem: AddSkillData): Boolean {
                return oldItem.name == newItem.name && oldItem.isAdded == newItem.isAdded
            }
        }
    }

    override fun submitList(list: MutableList<AddSkillData>?) {
        super.submitList(if (list != null) ArrayList(list) else null)
    }

//    override fun submitList(list: List<AddSkillData>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }
}