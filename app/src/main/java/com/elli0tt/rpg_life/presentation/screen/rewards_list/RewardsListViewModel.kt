package com.elli0tt.rpg_life.presentation.screen.rewards_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.model.Reward
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class RewardsListViewModel @Inject constructor() : ViewModel() {

    private var _rewardsList = MutableLiveData<List<Reward>>()
    val rewardsList: LiveData<List<Reward>> = _rewardsList

    init {
        loadData()
    }

    private fun loadData() {
        _rewardsList.value = generateMockRewardsSize()
    }

    private fun generateMockRewardsSize(): List<Reward> {
        val size = 50
        val result = ArrayList<Reward>(size)

        repeat(size) {
            result.add(
                Reward(
                    name = "+1 глава книги в день",
                    startTimeInMillis = Calendar.getInstance().timeInMillis,
                    endTimeInMillis = Calendar.getInstance().timeInMillis
                )
            )
        }

        return result
    }
}