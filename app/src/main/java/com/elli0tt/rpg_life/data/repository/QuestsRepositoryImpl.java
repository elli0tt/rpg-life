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
    public List<Quest> getQuestsByIds(List<Integer> ids) {
        return dao.getQuestsById(ids);
    }

    @Override
    public LiveData<List<Quest>> getAllQuests() {
        return dao.getAllQuests();
    }

    @Override
    public LiveData<List<Quest>> getSubQuests(int parentQuestId) {
        return dao.getSubQuests(parentQuestId);
    }

    @Override
    public LiveData<Quest> getQuestByIdLiveData(int questId) {
        return dao.getQuestByIdLiveData(questId);
    }

    @Override
    public void insert(Quest... quests) {
        new InsertOneAsyncTask(dao).execute(quests);
    }

    private static class InsertOneAsyncTask extends AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        InsertOneAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.insert(quests);
            return null;
        }
    }

    @Override
    public void update(Quest... quests) {
        new UpdateAsyncTask(dao).execute(quests);
    }

    private static class UpdateAsyncTask extends AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        UpdateAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.update(quests);
            return null;
        }
    }

    @Override
    public void delete(Quest... quests) {
        new DeleteAsyncTask(dao).execute(quests);
    }

    private static class DeleteAsyncTask extends AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        DeleteAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.delete(quests);
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

    @Override
    public boolean isShowCompleted() {
        return questsSharedPrefUtils.isShowCompeleted();
    }

    @Override
    public void setShowCompleted(boolean isShowCompleted) {
        questsSharedPrefUtils.setShowCompleted(isShowCompleted);
    }


}
