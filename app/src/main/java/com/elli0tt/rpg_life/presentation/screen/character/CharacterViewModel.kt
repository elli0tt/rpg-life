package com.elli0tt.rpg_life.presentation.screen.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.elli0tt.rpg_life.data.repository.CharacterRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Characteristic
import java.util.*

class CharacterViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository: CharacterRepositoryImpl = CharacterRepositoryImpl(application)
    private val allCharacteristics: LiveData<List<Characteristic>>

    private var _characterCoins = MutableLiveData<Int>(characterRepository.characterCoins)
    val characterCoins: LiveData<Int> = _characterCoins

    private var _snackbarTextResId = MutableLiveData<Int>()
    val snackbarTextResId: LiveData<Int> = _snackbarTextResId

    init {
        allCharacteristics = characterRepository.allCharacteristics
    }

    fun insert(characteristic: Characteristic) {
        characterRepository.insertCharacteristics(characteristic)
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
        characterRepository.insertCharacteristics(generateSampleCharacteristicsList())
    }

    fun addOneCoin() {
        _characterCoins.value = (_characterCoins.value ?: 0) + 1
        characterRepository.characterCoins = characterCoins.value ?: 0
    }

    fun takeOneCoin() {
        _characterCoins.value = (_characterCoins.value ?: 0) - 1
        characterRepository.characterCoins = characterCoins.value ?: 0
    }
}