package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.domain.model.Reward
import kotlinx.android.synthetic.main.list_item_rewards_shop.view.*

class RewardsShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(reward: Reward) {
        itemView.apply {
            rewardNameTextView.text = reward.name
            priceTextView.text = reward.price
        }
    }
}