package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.domain.modal.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestRepository;

import java.util.List;

public class QuestsRepositoryImpl implements QuestRepository {
    private QuestsDao dao;

    public QuestsRepositoryImpl(Application application){
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        dao = database.getQuestDao();
    }

    public Quest getQuestById(int id){
        return dao.getQuestById(id);
    }

    public LiveData<List<Quest>> getAllQuestsList() {
        return dao.getAllQuests();
    }

    public void insert(Quest quest){
        new InsertOneAsyncTask(dao).execute(quest);
    }

    private static class InsertOneAsyncTask extends AsyncTask<Quest, Void, Void>{
        private QuestsDao dao;

        InsertOneAsyncTask(QuestsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.insert(quests[0]);
            return null;
        }
    }

    public void insert(List<Quest> questList){
        new InsertListAsyncTask(dao).execute(questList);
    }

    private static class InsertListAsyncTask extends AsyncTask<List<Quest>, Void, Void>{
        private QuestsDao dao;

        InsertListAsyncTask(QuestsDao dao){
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Quest>... lists) {
            dao.insert(lists[0]);
            return null;
        }
    }

    public void update(Quest quest){
        new UpdateAsyncTask(dao).execute(quest);
    }

    private static class UpdateAsyncTask extends AsyncTask<Quest, Void, Void>{
        private QuestsDao dao;

        UpdateAsyncTask(QuestsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.update(quests[0]);
            return null;
        }
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(dao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private QuestsDao dao;

        DeleteAllAsyncTask(QuestsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

}
