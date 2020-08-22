package com.elli0tt.rpg_life.presentation.screen.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.UserRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Characteristic
import com.elli0tt.rpg_life.domain.model.User
import java.util.*

class CharacterViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepositoryImpl = UserRepositoryImpl(application)
    private val allCharacteristics: LiveData<List<Characteristic>>

    private var _user = MutableLiveData(userRepository.user)
    val user: LiveData<User> = _user

    private var _snackbarTextResId = MutableLiveData<Int>()
    val snackbarTextResId: LiveData<Int> = _snackbarTextResId

    init {
        allCharacteristics = userRepository.allCharacteristics
    }

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

    fun populateWithSamples() {
        userRepository.insertCharacteristics(generateSampleCharacteristicsList())
    }

    fun addOneCoin() {
        val newUser = _user.value!!
        newUser.coinsCount = newUser.coinsCount + 1
        _user.value = newUser
        userRepository.user = user.value!!
    }

    fun takeOneCoin() {
        val newUser = _user.value!!
        newUser.coinsCount = newUser.coinsCount - 1
        _user.value = newUser
        userRepository.user = user.value!!
    }
}