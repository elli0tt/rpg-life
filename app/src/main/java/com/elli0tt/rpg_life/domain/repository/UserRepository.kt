package com.elli0tt.rpg_life.domain.repository

import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.domain.model.Characteristic
import com.elli0tt.rpg_life.domain.model.User

interface UserRepository {
    val allCharacteristics: LiveData<List<Characteristic>>

    fun insertCharacteristics(characteristic: Characteristic)
    fun insertCharacteristics(characteristicList: List<Characteristic>)
    fun deleteCharacteristic()
    fun updateCharacteristic()

    var user: User
}