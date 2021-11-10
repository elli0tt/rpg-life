package com.elli0tt.rpg_life.presentation.screen.rewards_shop

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.data.repository.UserRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Reward
import com.elli0tt.rpg_life.presentation.core.live_data.Event
import javax.inject.Inject

class RewardsShopViewModel @Inject constructor(
        private val userRepository: UserRepositoryImpl
) : ViewModel() {

    private var _rewardsList = MutableLiveData<List<Reward>>()
    val rewardsList: LiveData<List<Reward>> = _rewardsList

    private var _showSnackbarEvent = MutableLiveData<Event<String>>()
    val showSnackbarEvent: LiveData<Event<String>> = _showSnackbarEvent

    init {
        loadData()
    }

    private fun loadData() {
        _rewardsList.value = generateRewardsMockList()
    }

    private fun generateRewardsMockList(): List<Reward> {
        return arrayListOf(
                Reward(name = "1 глава книги", price = 10),
                Reward(name = "1 глава аудиокниги", price = 10),
                Reward(name = "Подкаст", price = 10),
                Reward(name = "1 серия сериала", price = 20),
                Reward(name = "1 фильм", price = 30),
                Reward(name = "1 партия в шахматы", price = 10),
                Reward(name = "1 час игры в компьютерную игру", price = 20),
                Reward(name = "Музыка на весь день", price = 20),
                Reward(name = "Зайти в инстаграм", price = 50),
                Reward(name = "Полистать ленту вк", price = 50),
                Reward(name = "Посмотреть видео на Youtube", price = 50)
        )
    }

    fun buyReward(position: Int) {
        val newUser = userRepository.user
        rewardsList.value?.get(position)?.let { reward ->
            val price = reward.price
            newUser.coinsCount = newUser.coinsCount - price
            userRepository.user = newUser
            _showSnackbarEvent.value = Event(reward.name)
        }
    }
}
