package com.elli0tt.rpg_life.presentation.adapter.rewards_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Reward
import com.elliot.patientapp.presentation.adapter.base.BaseListAdapter

class RewardsListAdapter : BaseListAdapter<Reward, RewardsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_reward, parent, false)
        return RewardsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RewardsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
