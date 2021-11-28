package com.elli0tt.rpg_life.presentation.adapter.rewards_shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.domain.model.OldReward
import kotlinx.android.synthetic.main.list_item_rewards_shop.view.*

class RewardsShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(reward: OldReward, onItemClickListener: RewardsShopAdapter.OnItemClickListener?) {
        itemView.apply {
            rewardNameTextView.text = reward.name
            priceTextView.text = reward.price.toString()

            setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }
}