package com.elli0tt.rpg_life.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.QuestsDao;
import com.elli0tt.rpg_life.data.dao.RelatedToQuestsSkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.QuestsFilterState;
import com.elli0tt.rpg_life.domain.model.QuestsSortingState;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

import javax.inject.Inject;

public class QuestsRepositoryImpl implements QuestsRepository {
    private QuestsDao questsDao;
    private RelatedToQuestsSkillsDao relatedToQuestsSkillsDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Inject
    public QuestsRepositoryImpl(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(context);
        questsDao = database.getQuestDao();
        relatedToQuestsSkillsDao = database.getRelatedToQuestSkillsDao();
        sharedPreferencesUtils = new SharedPreferencesUtils(context);
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
    public void insertQuests(Quest... quests) {
        new InsertQuestsAsyncTask(questsDao).execute(quests);
    }

    @Override
    public void updateQuests(Quest... quests) {
        new UpdateQuestsAsyncTask(questsDao).execute(quests);
    }

    @Override
    public void updateQuestHasSubquestsById(int id, boolean hasSubquests) {
        new UpdateQuestHasSubquestByIdAsyncTask(questsDao).execute(new Pair<>(id, hasSubquests));
    }

    @Override
    public void deleteQuests(Quest... quests) {
        new DeleteQuestsAsyncTask(questsDao).execute(quests);
    }

    @Override
    public void deleteAllQuests() {
        new DeleteAllQuestsAsyncTask(questsDao).execute();
    }

    @Override
    public QuestsFilterState getQuestsFilterState() {
        return sharedPreferencesUtils.getQuestsFilterState();
    }

    @Override
    public void setQuestsFilterState(QuestsFilterState filterState) {
        sharedPreferencesUtils.setQuestsFilterState(filterState);
    }

    @Override
    public QuestsSortingState getQuestsSortingState() {
        return sharedPreferencesUtils.getQuestsSortingState();
    }

    @Override
    public void setQuestsSoringState(QuestsSortingState sortingState) {
        sharedPreferencesUtils.setQuestsSortingState(sortingState);
    }

    @Override
    public boolean isShowCompleted() {
        return sharedPreferencesUtils.isShowCompleted();
    }

    @Override
    public void setShowCompleted(boolean isShowCompleted) {
        sharedPreferencesUtils.setShowCompleted(isShowCompleted);
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

    @Override
    public void deleteRelatedSkill(int questId, int skillId) {
        new DeleteRelatedSkillAsyncTask(relatedToQuestsSkillsDao).execute(new RelatedToQuestSkills(questId, skillId));
    }

    @Override
    public int getRelatedSkillId(int questId) {
        return relatedToQuestsSkillsDao.getRelatedSkillId(questId);
    }

    @Override
    public void insertQuestWithRelatedSkills(Quest quest, int oldQuestId) {
        new InsertQuestWithRelatedSkillsAsyncTask(questsDao, relatedToQuestsSkillsDao).execute(new Pair<>(quest, oldQuestId));
    }

    @Override
    public long insertEmptyQuestSync() {
        Quest quest = new Quest();
        quest.setName("");
        return questsDao.insertQuests(quest).get(0);
    }

    @Override
    public long insertEmptyChallengeSync() {
        Quest challenge = new Quest();
        challenge.setName("");
        challenge.setChallenge(true);
        return questsDao.insertQuests(challenge).get(0);
    }

    @Override
    public long insertEmptySubQuestSync(int parentQuestId) {
        Quest subquest = new Quest();
        subquest.setName("");
        subquest.setSubQuest(true);
        subquest.setParentQuestId(parentQuestId);
        return questsDao.insertQuests(subquest).get(0);
    }

    private static class InsertQuestsAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        InsertQuestsAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.insertQuests(quests);
            return null;
        }
    }

    private static class UpdateQuestsAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        UpdateQuestsAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.updateQuests(quests);
            return null;
        }
    }

    private static class UpdateQuestHasSubquestByIdAsyncTask extends android.os.AsyncTask<Pair<Integer,
            Boolean>, Void, Void> {
        private QuestsDao dao;

        UpdateQuestHasSubquestByIdAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Pair<Integer, Boolean>... value) {
            if (value[0].first != null && value[0].second != null) {
                dao.updateHasSubquestsById(value[0].first, value[0].second);
            }
            return null;
        }
    }

    private static class DeleteQuestsAsyncTask extends android.os.AsyncTask<Quest, Void, Void> {
        private QuestsDao dao;

        DeleteQuestsAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Quest... quests) {
            dao.deleteQuests(quests);
            return null;
        }
    }

    private static class DeleteAllQuestsAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private QuestsDao dao;

        DeleteAllQuestsAsyncTask(QuestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllQuests();
            return null;
        }
    }

    private static class InsertRelatedSkillAsyncTask extends AsyncTask<RelatedToQuestSkills, Void
            , Void> {
        private RelatedToQuestsSkillsDao dao;

        InsertRelatedSkillAsyncTask(RelatedToQuestsSkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RelatedToQuestSkills... relatedToQuestSkills) {
            dao.insertRelatedSkill(relatedToQuestSkills[0]);
            return null;
        }
    }

    private static class DeleteRelatedSkillAsyncTask extends AsyncTask<RelatedToQuestSkills, Void
            , Void> {
        private RelatedToQuestsSkillsDao dao;

        DeleteRelatedSkillAsyncTask(RelatedToQuestsSkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RelatedToQuestSkills... relatedToQuestSkills) {
            dao.deleteRelatedSkill(relatedToQuestSkills[0].getQuestId(),
                    relatedToQuestSkills[0].getSkillId());
            return null;
        }
    }

    private static class InsertQuestWithRelatedSkillsAsyncTask extends android.os.AsyncTask<Pair<Quest, Integer>, Void, Void> {
        private QuestsDao questsDao;
        private RelatedToQuestsSkillsDao relatedToQuestsSkillsDao;

        InsertQuestWithRelatedSkillsAsyncTask(QuestsDao questsDao,
                                              RelatedToQuestsSkillsDao relatedToQuestsSkillsDao) {
            this.questsDao = questsDao;
            this.relatedToQuestsSkillsDao = relatedToQuestsSkillsDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Pair<Quest, Integer>... pairs) {
            List<Long> ids = questsDao.insertQuests(pairs[0].first);
            if (pairs[0].second != null) {
                for (long id : ids) {
                    relatedToQuestsSkillsDao.copyRelatedSkills(pairs[0].second, (int) id);
                }
            }

            return null;
        }
    }
}