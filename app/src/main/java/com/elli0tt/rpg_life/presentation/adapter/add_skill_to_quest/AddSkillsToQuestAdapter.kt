package com.elli0tt.rpg_life.presentation.adapter.add_skill_to_quest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.AddSkillData

class AddSkillsToQuestAdapter :
    ListAdapter<AddSkillData, AddSkillsToQuestViewHolder>(DIFF_CALLBACK) {

    interface OnXpPercentageSeekBarTouchStopListener {
        fun onTouchStop(position: Int, xpPercentage: Int)
    }

    lateinit var onXpPercentageSeekBarTouchStopListener: OnXpPercentageSeekBarTouchStopListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSkillsToQuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_add_skills_to_quest, parent, false)
        return AddSkillsToQuestViewHolder(view, onXpPercentageSeekBarTouchStopListener)
    }

    override fun onBindViewHolder(holder: AddSkillsToQuestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<AddSkillData> =
            object : DiffUtil.ItemCallback<AddSkillData>() {
                override fun areItemsTheSame(
                    oldItem: AddSkillData,
                    newItem: AddSkillData
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AddSkillData,
                    newItem: AddSkillData
                ): Boolean {
                    return oldItem.name == newItem.name &&
                            oldItem.xpPercentage == newItem.xpPercentage
                }
            }
    }

    override fun submitList(list: List<AddSkillData>?) {
        super.submitList(if (list != null) ArrayList(list) else null)
    }

//    override fun submitList(list: List<AddSkillData>?) {
//        super.submitList(list?.let { ArrayList(it) })
//    }
}