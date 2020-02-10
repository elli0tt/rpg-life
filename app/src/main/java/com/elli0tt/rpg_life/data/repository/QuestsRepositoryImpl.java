package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.data.shared_prefs.QuestsSharedPrefUtils;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

import java.util.List;

public class QuestsRepositoryImpl implements QuestsRepository {
    private QuestsDao dao;
    private QuestsSharedPrefUtils questsSharedPrefUtils;

    public QuestsRepositoryImpl(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        dao = database.getQuestDao();
        questsSharedPrefUtils = new QuestsSharedPrefUtils(application);
    }

    @Override
    public Quest getQuestById(int id) {
        return dao.getQuestById(id);
    }

    @Override
    public LiveData<List<Quest>> getAllQuests() {
        return dao.getAllQuests();
    }

    @Override
    public LiveData<List<Quest>> getActiveQuests() {
        return dao.getActiveQuests();
    }

    @Override
    public LiveData<List<Quest>> getCompletedQuests() {
        return dao.getCompletedQuests();
    }

    @Override
    public LiveData<List<Quest>> getImportantQuests() {
        return dao.getImportantQuests();
    }

    @Override
    public void insert(Quest quest) {
        new InsertOneAsyncTask(dao).execute(quest);
    }

    private static class InsertOneAsyncTask extends AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        InsertOneAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.insert(quests[0]);
            return null;
        }
    }

    @Override
    public void insert(List<Quest> questList) {
        new InsertListAsyncTask(dao).execute(questList);
    }

    private static class InsertListAsyncTask extends AsyncTask<List<Quest>, Void, Void> {
        private QuestsDao dao;

        InsertListAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Quest>... lists) {
            dao.insert(lists[0]);
            return null;
        }
    }

    @Override
    public void update(Quest quest) {
        new UpdateAsyncTask(dao).execute(quest);
    }

    private static class UpdateAsyncTask extends AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        UpdateAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.update(quests[0]);
            return null;
        }
    }

    @Override
    public void delete(List<Quest> questList) {
        new DeleteAsyncTask(dao).execute(questList);
    }

    private static class DeleteAsyncTask extends AsyncTask<List<Quest>, Void, Void> {
        private QuestsDao dao;

        DeleteAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(List<Quest>... lists) {
            dao.delete(lists[0]);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private QuestsDao dao;

        DeleteAllAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    @Override
    public QuestsFilterState getQuestsFilterState() {
        return questsSharedPrefUtils.getQuestsFilterState();
    }

    @Override
    public void setQuestsFilterState(QuestsFilterState filterState) {
        questsSharedPrefUtils.setQuestsFilterState(filterState);
    }

    @Override
    public QuestsSortingState getQuestSortingState() {
        return questsSharedPrefUtils.getQuestsSortingState();
    }

    @Override
    public void setQuestsSoringState(QuestsSortingState sortingState) {
        questsSharedPrefUtils.setQuestsSortingState(sortingState);
    }
}
