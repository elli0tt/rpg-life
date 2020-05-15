package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.dao.RelatedToQuestsSkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.data.shared_prefs.QuestsSharedPrefUtils;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

import java.util.List;

public class QuestsRepositoryImpl implements QuestsRepository {
    private QuestsDao questsDao;
    private RelatedToQuestsSkillsDao relatedToQuestsSkillsDao;
    private QuestsSharedPrefUtils questsSharedPrefUtils;

    public QuestsRepositoryImpl(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        questsDao = database.getQuestDao();
        relatedToQuestsSkillsDao = database.getRelatedToQuestSkillsDao();
        questsSharedPrefUtils = new QuestsSharedPrefUtils(application);
    }

    @Override
    public Quest getQuestById(int id) {
        return questsDao.getQuestById(id);
    }

    @Override
    public List<Quest> getQuestsByIds(List<Integer> ids) {
        return questsDao.getQuestsById(ids);
    }

    @Override
    public LiveData<List<Quest>> getAllQuests() {
        return questsDao.getAllQuests();
    }

    @Override
    public LiveData<List<Quest>> getSubQuests(int parentQuestId) {
        return questsDao.getSubQuests(parentQuestId);
    }

    @Override
    public LiveData<Quest> getQuestByIdLiveData(int questId) {
        return questsDao.getQuestByIdLiveData(questId);
    }

    @Override
    public void insert(Quest... quests) {
        new InsertQuestsAsyncTask(questsDao).execute(quests);
    }

    private static class InsertQuestsAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        InsertQuestsAsyncTask(QuestsDao dao) {
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
        new UpdateAsyncTask(questsDao).execute(quests);
    }

    private static class UpdateAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
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
    public void updateQuestHasSubquestsById(int id, boolean hasSubquests) {
        new UpdateQuestHasSubquestByIdAsyncTask(questsDao).execute(new Pair<>(id, hasSubquests));
    }

    private static class UpdateQuestHasSubquestByIdAsyncTask extends android.os.AsyncTask<Pair<Integer,
            Boolean>, Void, Void> {
        private QuestsDao dao;

        UpdateQuestHasSubquestByIdAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Pair<Integer, Boolean>... value) {
            dao.updateHasSubquestsById(value[0].first, value[0].second);
            return null;
        }
    }

    @Override
    public void delete(Quest... quests) {
        new DeleteAsyncTask(questsDao).execute(quests);
    }

    private static class DeleteAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
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
        new DeleteAllAsyncTask(questsDao).execute();
    }

    private static class DeleteAllAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
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
    public QuestsSortingState getQuestsSortingState() {
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

    @Override
    public LiveData<List<RelatedToQuestSkills>> getRelatedSkillsLiveData(int questId) {
        return relatedToQuestsSkillsDao.getRelatedSkillsLiveData(questId);
    }

    @Override
    public List<RelatedToQuestSkills> getRelatedSkills(int questId) {
        return relatedToQuestsSkillsDao.getRelatedSkills(questId);
    }

    @Override
    public void insertRelatedSkill(int questId, int skillId, int xpPercentage) {
        new InsertRelatedSkillAsyncTask(relatedToQuestsSkillsDao).execute(new RelatedToQuestSkills(questId, skillId, xpPercentage));
    }

    private static class InsertRelatedSkillAsyncTask extends AsyncTask<RelatedToQuestSkills, Void
            , Void> {
        private RelatedToQuestsSkillsDao dao;

        InsertRelatedSkillAsyncTask(RelatedToQuestsSkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RelatedToQuestSkills... relatedToQuestSkills) {
            dao.insert(relatedToQuestSkills[0]);
            return null;
        }
    }

    @Override
    public void deleteRelatedSkill(int questId, int skillId) {
        new DeleteRelatedSkillAsyncTask(relatedToQuestsSkillsDao).execute(new RelatedToQuestSkills(questId, skillId));
    }

    private static class DeleteRelatedSkillAsyncTask extends AsyncTask<RelatedToQuestSkills, Void
            , Void> {
        private RelatedToQuestsSkillsDao dao;

        DeleteRelatedSkillAsyncTask(RelatedToQuestsSkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RelatedToQuestSkills... relatedToQuestSkills) {
            dao.delete(relatedToQuestSkills[0].getQuestId(), relatedToQuestSkills[0].getSkillId());
            return null;
        }
    }

    @Override
    public int getCurrentId() {
        return questsSharedPrefUtils.getCurrentId();
    }

    @Override
    public void setCurrentId(int id) {
        questsSharedPrefUtils.setCurrentId(id);
    }

    @Override
    public int getRelatedSkillId(int questId) {
        return relatedToQuestsSkillsDao.getRelatedSkillId(questId);
    }

}