package com.elli0tt.rpg_life.presentation.screen.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elli0tt.rpg_life.data.repository.UserRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Characteristic
import com.elli0tt.rpg_life.domain.model.User
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class CharacterViewModel @Inject constructor(
        private val userRepository: UserRepositoryImpl
) : ViewModel() {

    private val allCharacteristics: LiveData<List<Characteristic>> = userRepository.allCharacteristics

    private var _user = MutableLiveData(userRepository.user)
    val user: LiveData<User> = _user

    private var _snackbarTextResId = MutableLiveData<Int>()
    val snackbarTextResId: LiveData<Int> = _snackbarTextResId

    private var coinsMultiplier: Double = MULTIPLIER_NONE

    fun insert(characteristic: Characteristic) {
        userRepository.insertCharacteristics(characteristic)
    }

    private fun generateSampleCharacteristicsList(): List<Characteristic> {
        val resultList: MutableList<Characteristic> = ArrayList()
        resultList.add(Characteristic("Strength"))
        resultList.add(Characteristic("Intelligence"))
        resultList.add(Characteristic("Agility"))
        resultList.add(Characteristic("Endurance"))
        resultList.add(Characteristic("Willpower"))
        resultList.add(Characteristic("Procrastination"))
        resultList.add(Characteristic("Self-confidence"))
        resultList.add(Characteristic("Communication"))
        return resultList
    }

    fun onPopulateWithSamplesClick() {
        userRepository.insertCharacteristics(generateSampleCharacteristicsList())
    }

    fun onIncreaseCoinsClick() {
        changeUserCoinsCountBy(1)
    }

    fun onDecreaseCoinsClick() {
        changeUserCoinsCountBy(-1)
    }

    fun onMultiplierNoneClick() {
        coinsMultiplier = MULTIPLIER_NONE
    }

    fun onMultiplierX1_5Click() {
        coinsMultiplier = MULTIPLIER_X_1_5
    }

    fun onMultiplierX2Click() {
        coinsMultiplier = MULTIPLIER_X_2
    }

    fun onCompleteVeryEasyTaskClick() {
        changeUserCoinsCountByWithMultiplier(VERY_EASY_TASK_COINS_REWARD)
    }

    fun onCompleteEasyTaskClick() {
        changeUserCoinsCountByWithMultiplier(EASY_TASK_COINS_REWARD)
    }

    fun onCompleteNormalTaskClick() {
        changeUserCoinsCountByWithMultiplier(NORMAL_TASK_COINS_REWARD)
    }

    fun onCompleteHardTaskClick() {
        changeUserCoinsCountByWithMultiplier(HARD_TASK_COINS_REWARD)
    }

    fun onCompleteEpicTaskClick() {
        changeUserCoinsCountByWithMultiplier(EPIC_TASK_COINS_REWARD)
    }

    fun onCompleteLegendaryTaskClick() {
        changeUserCoinsCountByWithMultiplier(LEGENDARY_TASK_COINS_REWARD)
    }

    private fun changeUserCoinsCountByWithMultiplier(value: Int) {
        val changeValue: Int = (value * coinsMultiplier).roundToInt()
        changeUserCoinsCountBy(changeValue)
    }

    private fun changeUserCoinsCountBy(value: Int) {
        val newUser = _user.value!!
        newUser.coinsCount = newUser.coinsCount + value
        _user.value = newUser
        userRepository.user = user.value!!
    }

    companion object {
        private const val VERY_EASY_TASK_COINS_REWARD = 1
        private const val EASY_TASK_COINS_REWARD = 3
        private const val NORMAL_TASK_COINS_REWARD = 5
        private const val HARD_TASK_COINS_REWARD = 10
        private const val EPIC_TASK_COINS_REWARD = 20
        private const val LEGENDARY_TASK_COINS_REWARD = 50

        private const val MULTIPLIER_NONE = 1.0
        private const val MULTIPLIER_X_1_5 = 1.5
        private const val MULTIPLIER_X_2 = 2.0
    }
}