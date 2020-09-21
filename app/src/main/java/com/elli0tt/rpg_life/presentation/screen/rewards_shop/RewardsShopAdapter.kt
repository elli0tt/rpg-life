package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Reward

class RewardsShopAdapter : ListAdapter<Reward, RewardsShopViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Reward>() {
            override fun areItemsTheSame(oldItem: Reward, newItem: Reward): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Reward, newItem: Reward): Boolean {
                return true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsShopViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_rewards_shop, parent, false)
        return RewardsShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardsShopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}