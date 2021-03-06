package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.domain.model.Reward
import javax.inject.Inject

class RewardsShopViewModel @Inject constructor() : ViewModel() {

    private var _rewardsList = MutableLiveData<List<Reward>>()
    val rewardsList: LiveData<List<Reward>> = _rewardsList

    init {
        loadData()
    }

    private fun loadData() {
        _rewardsList.value = generateRewardsMockList()
    }

    private fun generateRewardsMockList(): List<Reward> {
        return arrayListOf(Reward(name = "Watch Youtube videos", price = "1 minute", boughtCount = 5),
                Reward(name = "Watch film / serial", price = "1 minute", boughtCount = 5),
                Reward(name = "Read book", price = "5 minutes", boughtCount = 5),
                Reward(name = "Listen to audio book", price = "5 minutes", boughtCount = 5),
                Reward(name = "Play computer game", price = "1 minute", boughtCount = 5),
                Reward(name = "Look through social networks news feed", price = "1 minute", boughtCount = 5))
    }
}