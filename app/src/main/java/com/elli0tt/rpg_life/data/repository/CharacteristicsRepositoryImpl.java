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

    public LiveData<List<Characteristic>> getAllCharacteristics() {
        return characteristicsDao.getAllCharacteristics();
    }

    @Override
    public void insert(Characteristic characteristic) {
        new InsertOneAsyncTask(characteristicsDao).execute(characteristic);
    }

    private static class InsertOneAsyncTask extends AsyncTask<Characteristic, Void, Void> {
        private CharacteristicsDao characteristicsDao;

        InsertOneAsyncTask(CharacteristicsDao characteristicsDao) {
            this.characteristicsDao = characteristicsDao;
        }

        @Override
        protected Void doInBackground(Characteristic... characteristics) {
            characteristicsDao.insert(characteristics[0]);
            return null;
        }
    }

    @Override
    public void insert(List<Characteristic> characteristicList) {
        new InsertListAsyncTask(characteristicsDao).execute(characteristicList);
    }

    private static class InsertListAsyncTask extends AsyncTask<List<Characteristic>, Void, Void> {
        private CharacteristicsDao characteristicsDao;

        InsertListAsyncTask(CharacteristicsDao characteristicsDao) {
            this.characteristicsDao = characteristicsDao;
        }


        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Characteristic>... lists) {
            characteristicsDao.insert(lists[0]);
            return null;
        }
    }

    @Override
    public void delete() {

    }

    @Override
    public void update() {

    }


}
