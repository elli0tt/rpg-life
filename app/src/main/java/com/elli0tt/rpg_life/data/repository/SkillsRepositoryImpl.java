package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

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

    public SkillsRepositoryImpl(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        skillsDao = database.getSkillsDao();
        skillsSharedPrefUtils = new SkillsSharedPrefUtils(application);
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

    private static class UpdateAsyncTask extends AsyncTask<Skill, Void, Void> {
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

    private static class UpdateTotalXpByIdAsyncTask extends AsyncTask<Pair<Integer, Long>, Void, Void> {
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

}
