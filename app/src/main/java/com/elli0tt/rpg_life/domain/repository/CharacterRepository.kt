package com.elli0tt.rpg_life.domain.repository

import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.domain.model.Characteristic

interface CharacterRepository {
    val allCharacteristics: LiveData<List<Characteristic>>

    fun insertCharacteristics(characteristic: Characteristic)
    fun insertCharacteristics(characteristicList: List<Characteristic>)
    fun deleteCharacteristic()
    fun updateCharacteristic()

    var characterCoins: Int
}