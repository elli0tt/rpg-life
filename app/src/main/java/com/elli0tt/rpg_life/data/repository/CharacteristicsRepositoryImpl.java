package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.CharacteristicsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.domain.model.Characteristic;
import com.elli0tt.rpg_life.domain.repository.CharacteristicsRepository;

import java.util.List;

public class CharacteristicsRepositoryImpl implements CharacteristicsRepository {
    private CharacteristicsDao characteristicsDao;

    public CharacteristicsRepositoryImpl(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        characteristicsDao = database.getCharacteristicsDao();
    }

    @Override
    public LiveData<List<Characteristic>> getAllCharacteristics() {
        return characteristicsDao.getAllCharacteristics();
    }

    @Override
    public void insertCharacteristics(Characteristic characteristic) {
        new InsertCharacteristicAsyncTask(characteristicsDao).execute(characteristic);
    }

    private static class InsertCharacteristicAsyncTask extends AsyncTask<Characteristic, Void, Void> {
        private CharacteristicsDao characteristicsDao;

        InsertCharacteristicAsyncTask(CharacteristicsDao characteristicsDao) {
            this.characteristicsDao = characteristicsDao;
        }

        @Override
        protected Void doInBackground(Characteristic... characteristics) {
            characteristicsDao.insertCharacteristic(characteristics[0]);
            return null;
        }
    }

    @Override
    public void insertCharacteristics(List<Characteristic> characteristicList) {
        new InsertCharacteristicsAsyncTask(characteristicsDao).execute(characteristicList);
    }

    private static class InsertCharacteristicsAsyncTask extends AsyncTask<List<Characteristic>, Void, Void> {
        private CharacteristicsDao characteristicsDao;

        InsertCharacteristicsAsyncTask(CharacteristicsDao characteristicsDao) {
            this.characteristicsDao = characteristicsDao;
        }


        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Characteristic>... lists) {
            characteristicsDao.insertCharacteristic(lists[0]);
            return null;
        }
    }

    @Override
    public void deleteCharacteristic() {

    }

    @Override
    public void updateCharacteristic() {

    }


}
