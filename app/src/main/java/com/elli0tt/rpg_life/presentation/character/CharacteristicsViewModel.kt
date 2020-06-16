package com.elli0tt.rpg_life.presentation.character

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.data.repository.CharacteristicsRepositoryImpl
import com.elli0tt.rpg_life.domain.model.Characteristic
import java.util.*

class CharacteristicsViewModel(application: Application) : AndroidViewModel(application) {
    private val characteristicsRepository: CharacteristicsRepositoryImpl = CharacteristicsRepositoryImpl(application)
    private val allCharacteristics: LiveData<List<Characteristic>>

    init {
        allCharacteristics = characteristicsRepository.allCharacteristics
    }

    fun insert(characteristic: Characteristic?) {
        characteristicsRepository.insertCharacteristics(characteristic)
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
        characteristicsRepository.insertCharacteristics(generateSampleCharacteristicsList())
    }
}