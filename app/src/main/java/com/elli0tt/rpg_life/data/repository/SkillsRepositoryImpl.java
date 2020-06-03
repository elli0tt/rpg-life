package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.data.shared_prefs.SkillsSharedPrefUtils;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.presentation.skills.SkillsSortingState;

import java.util.List;

public class SkillsRepositoryImpl implements SkillsRepository {
    private SkillsDao skillsDao;
    private SkillsSharedPrefUtils skillsSharedPrefUtils;

    public SkillsRepositoryImpl(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(context);
        skillsDao = database.getSkillsDao();
        skillsSharedPrefUtils = new SkillsSharedPrefUtils(context);
    }

    @Override
    public void insertSkills(Skill... skills) {
        new InsertAsyncTask(skillsDao).execute(skills);
    }

    private static class InsertAsyncTask extends android.os.AsyncTask<Skill, Void, Void> {
        private SkillsDao dao;

        InsertAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.insertSkill(skills);
            return null;
        }
    }

    @Override
    public LiveData<List<Skill>> getAllSkills() {
        return skillsDao.getAllSkills();
    }

    @Override
    public List<String> getSkillsNamesByIds(List<Integer> ids) {
        return skillsDao.getSkillsNamesByIds(ids);
    }

    @Override
    public void updateSkills(Skill... skills){
        new UpdateAsyncTask(skillsDao).execute(skills);
    }

    private static class UpdateAsyncTask extends android.os.AsyncTask<Skill, Void, Void> {
        private SkillsDao dao;

        UpdateAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.updateSkill(skills);
            return null;
        }
    }

    @Override
    public void updateSkillTotalXpById(int id, long xpIncrease){
        new UpdateTotalXpByIdAsyncTask(skillsDao).execute(new Pair<>(id, xpIncrease));
    }

    private static class UpdateTotalXpByIdAsyncTask extends android.os.AsyncTask<Pair<Integer, Long>, Void, Void> {
        private SkillsDao dao;

        UpdateTotalXpByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Pair<Integer, Long>... params) {
            dao.updateSkillTotalXpById(params[0].first, params[0].second);
            return null;
        }
    }

    @Override
    public void deleteAllSkills() {
        new DeleteAllAsyncTask(skillsDao).execute();
    }

    private static class DeleteAllAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private SkillsDao dao;

        DeleteAllAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllSkills();
            return null;
        }
    }

    @Override
    public SkillsSortingState getSkillsSortingState() {
        return skillsSharedPrefUtils.getQuestsSortingState();
    }

    @Override
    public void setSkillsSortingState(SkillsSortingState sortingState) {
        skillsSharedPrefUtils.setQuestsSortingState(sortingState);
    }

    @Override
    public Skill getSkillById(int skillId) {
        return skillsDao.getSkillById(skillId);
    }

    @Override
    public void updateSkillCategoryById(int skillId, int categoryId) {
        new UpdateSkillCategoryByIdAsyncTask(skillsDao).execute(new Pair<>(skillId, categoryId));
    }

    private static class UpdateSkillCategoryByIdAsyncTask extends android.os.AsyncTask<Pair<Integer, Integer>, Void, Void> {
        private SkillsDao dao;

        UpdateSkillCategoryByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Pair<Integer, Integer>... pairs) {
            dao.updateSkillCategoryById(pairs[0].first, pairs[0].second);
            return null;
        }
    }

    @Override
    public void deleteSkillsById(Integer... skillId) {
        new DeleteSkillsByIdAsyncTask(skillsDao).execute(skillId);
    }

    private static class DeleteSkillsByIdAsyncTask extends android.os.AsyncTask<Integer, Void, Void> {
        private SkillsDao dao;

        DeleteSkillsByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            dao.deleteSkillsById(ids);
            return null;
        }
    }

    @Override
    public long insertEmptySkill() {
        return skillsDao.insertSkill(new Skill(0, "")).get(0);
    }
}
