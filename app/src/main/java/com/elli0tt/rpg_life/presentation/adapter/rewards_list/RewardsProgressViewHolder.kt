package com.elli0tt.rpg_life.presentation.adapter.rewards_list

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.elli0tt.rpg_life.R
import com.elli0tt.rpg_life.domain.constants.Constants
import com.elli0tt.rpg_life.domain.model.RewardProgress
import kotlinx.android.synthetic.main.list_item_reward_progress.view.*
import java.text.SimpleDateFormat
import java.util.*

class RewardsProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

    fun bind(rewardProgress: RewardProgress) {
        itemView.nameTextView.text = rewardProgress.name
        itemView.dateRangeTextView.text = itemView.context.getString(
            R.string.rewards_progress_list_date_range,
            dateFormat.format(Date(rewardProgress.startTimeInMillis)),
            dateFormat.format(Date(rewardProgress.endTimeInMillis))
        )

        val progressPercent = calculateProgressPercent(rewardProgress)
        itemView.progressBar.progress = progressPercent
        itemView.progressBar.progressTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    itemView.context, resolveProgressBarColorRes(progressPercent)
                )
            )

        itemView.percentProgressTextView.text = itemView.context.getString(
            R.string.rewards_progress_list_percent_progress,
            progressPercent
        )

        val daysLeft = calculateDaysLeft(rewardProgress)
        itemView.daysLeftTextView.text =
            itemView.context.resources.getQuantityString(
                R.plurals.rewards_progress_list_days_left,
                daysLeft,
                daysLeft
            )
    }

    private fun calculateProgressPercent(rewardProgress: RewardProgress): Int {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        with(rewardProgress) {
            val result =
                ((currentTimeInMillis - startTimeInMillis).toDouble() / (endTimeInMillis - startTimeInMillis) * 100)
                    .toInt()
            if (result > MAX_PERCENT) {
                return MAX_PERCENT
            }
            if (result < 0) {
                return 0
            }
            return result
        }
    }

    private fun calculateDaysLeft(rewardProgress: RewardProgress): Int {
        val millisLeft = rewardProgress.endTimeInMillis - Calendar.getInstance().timeInMillis
        val daysLeft = millisLeft.toDouble() / Constants.MILLIS_IN_24_HOURS
        if (daysLeft < 0) {
            return 0
        }
        return daysLeft.toInt()
    }

    @ColorRes
    private fun resolveProgressBarColorRes(progress: Int): Int {
        return if (progress < itemView.progressBar.max) {
            R.color.colorSecondary
        } else {
            R.color.colorLightGreen
        }
    }

    companion object {
        private const val MAX_PERCENT = 100
    }
}
