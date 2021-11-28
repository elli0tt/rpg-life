package com.elli0tt.rpg_life.presentation.screen.rewards_progress_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.model.RewardProgress
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RewardsProgressListViewModel @Inject constructor() : ViewModel() {

    private var _rewardsList = MutableLiveData<List<RewardProgress>>()
    val rewardsList: LiveData<List<RewardProgress>> = _rewardsList

    init {
        loadData()
    }

    private fun loadData() {
        _rewardsList.value = generateMockRewardsSize()
    }

    private fun generateMockRewardsSize(): List<RewardProgress> {
        val size = 50
        val result = ArrayList<RewardProgress>(size)

        repeat(size) {
            result.add(
                RewardProgress(
                    name = "+1 глава книги в день",
                    startTimeInMillis = Calendar.getInstance().timeInMillis,
                    endTimeInMillis = Calendar.getInstance().timeInMillis
                )
            )
        }

        return result
    }
}