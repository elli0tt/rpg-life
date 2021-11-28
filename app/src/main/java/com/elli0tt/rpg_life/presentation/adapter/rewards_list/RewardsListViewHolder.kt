package com.elli0tt.rpg_life.presentation.adapter.rewards_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.model.Reward
import kotlinx.android.synthetic.main.list_item_reward.view.*
import java.text.SimpleDateFormat
import java.util.*

class RewardsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    fun bind(reward: Reward) {
        itemView.name_text_view.text = reward.name
        itemView.date_text_view.text = itemView.context.getString(
            R.string.rewards_list_date,
            dateFormat.format(Date(reward.startTimeInMillis)),
            dateFormat.format(Date(reward.endTimeInMillis))
        )
    }
}