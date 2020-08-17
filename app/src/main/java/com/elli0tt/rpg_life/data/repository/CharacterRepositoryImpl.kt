package com.elli0tt.rpg_life.data.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.elli0tt.rpg_life.data.dao.CharacteristicsDao
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase
import com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils
import com.elli0tt.rpg_life.domain.model.Characteristic
import com.elli0tt.rpg_life.domain.repository.CharacterRepository

class CharacterRepositoryImpl(application: Application?) : CharacterRepository {

    private val characteristicsDao: CharacteristicsDao

    private val sharedPreferencesUtils = SharedPreferencesUtils(application!!.applicationContext)

    init {
        val database = AppRoomDatabase.getDatabase(application)
        characteristicsDao = database.characteristicsDao
    }

    override val allCharacteristics: LiveData<List<Characteristic>>
        get() = characteristicsDao.allCharacteristics

    override fun insertCharacteristics(characteristic: Characteristic) {
        InsertCharacteristicAsyncTask(characteristicsDao).execute(characteristic)
    }

    private class InsertCharacteristicAsyncTask(private val characteristicsDao: CharacteristicsDao) : AsyncTask<Characteristic?, Void?, Void?>() {
        override fun doInBackground(vararg characteristics: Characteristic?): Void? {
            characteristicsDao.insertCharacteristic(characteristics[0])
            return null
        }
    }

    override fun insertCharacteristics(characteristicList: List<Characteristic>) {
        InsertCharacteristicsAsyncTask(characteristicsDao).execute(characteristicList)
    }

    private class InsertCharacteristicsAsyncTask(private val characteristicsDao: CharacteristicsDao) : AsyncTask<List<Characteristic?>?, Void?, Void?>() {
        @SafeVarargs
        override fun doInBackground(vararg lists: List<Characteristic?>?): Void? {
            characteristicsDao.insertCharacteristic(lists[0])
            return null
        }

    }

    override fun deleteCharacteristic() {}
    override fun updateCharacteristic() {}

    override var characterCoins: Int
        get() = sharedPreferencesUtils.characterCoins
        set(value) {
            sharedPreferencesUtils.characterCoins = value
        }
}