package com.elli0tt.rpg_life.presentation.adapter.rewards_shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.OldReward

class RewardsShopAdapter : ListAdapter<OldReward, RewardsShopViewHolder>(DIFF_CALLBACK) {

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardsShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rewards_shop, parent, false)
        return RewardsShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardsShopViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OldReward>() {
            override fun areItemsTheSame(oldItem: OldReward, newItem: OldReward): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: OldReward, newItem: OldReward): Boolean {
                return true
            }
        }
    }
}