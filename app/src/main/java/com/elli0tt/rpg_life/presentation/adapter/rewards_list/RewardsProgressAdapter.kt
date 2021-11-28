package com.elli0tt.rpg_life.presentation.adapter.rewards_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.RewardProgress
import com.elliot.patientapp.presentation.adapter.base.BaseListAdapter

class RewardsProgressAdapter : BaseListAdapter<RewardProgress, RewardsProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsProgressViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_reward_progress, parent, false)
        return RewardsProgressViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RewardsProgressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
