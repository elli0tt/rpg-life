package com.elli0tt.rpg_life.data.repository;

import android.content.Context;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.presentation.screen.skills.SkillsSortingState;

import java.util.List;

import javax.inject.Inject;

public class SkillsRepositoryImpl implements SkillsRepository {
    private SkillsDao skillsDao;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Inject
    public SkillsRepositoryImpl(Context context) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(context);
        skillsDao = database.getSkillsDao();
        sharedPreferencesUtils = new SharedPreferencesUtils(context);
    }

    @Override
    public void insertSkills(Skill... skills) {
        new InsertAsyncTask(skillsDao).execute(skills);
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
    public void updateSkills(Skill... skills) {
        new UpdateAsyncTask(skillsDao).execute(skills);
    }

    @Override
    public void updateSkillTotalXpById(int id, long xpIncrease) {
        new UpdateTotalXpByIdAsyncTask(skillsDao).execute(new Pair<>(id, xpIncrease));
    }

    @Override
    public void deleteAllSkills() {
        new DeleteAllAsyncTask(skillsDao).execute();
    }

    @Override
    public SkillsSortingState getSkillsSortingState() {
        return sharedPreferencesUtils.getSkillsSortingState();
    }

    @Override
    public void setSkillsSortingState(SkillsSortingState sortingState) {
        sharedPreferencesUtils.setSkillsSortingState(sortingState);
    }

    @Override
    public Skill getSkillById(int skillId) {
        return skillsDao.getSkillById(skillId);
    }

    @Override
    public void updateSkillCategoryById(int skillId, int categoryId) {
        new UpdateSkillCategoryByIdAsyncTask(skillsDao).execute(new Pair<>(skillId, categoryId));
    }

    @Override
    public void deleteSkillsById(Integer... skillId) {
        new DeleteSkillsByIdAsyncTask(skillsDao).execute(skillId);
    }

    @Override
    public long insertEmptySkill() {
        return skillsDao.insertSkill(new Skill(0, "")).get(0);
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

    private static class UpdateTotalXpByIdAsyncTask extends android.os.AsyncTask<Pair<Integer,
            Long>, Void, Void> {
        private SkillsDao dao;

        UpdateTotalXpByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Pair<Integer, Long>... params) {
            if (params[0].first != null && params[0].second != null) {
                dao.updateSkillTotalXpById(params[0].first, params[0].second);
            }
            return null;
        }
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

    private static class UpdateSkillCategoryByIdAsyncTask extends android.os.AsyncTask<Pair<Integer, Integer>, Void, Void> {
        private SkillsDao dao;

        UpdateSkillCategoryByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(Pair<Integer, Integer>... pairs) {
            if (pairs[0].first != null && pairs[0].second != null) {
                dao.updateSkillCategoryById(pairs[0].first, pairs[0].second);
            }
            return null;
        }
    }

    private static class DeleteSkillsByIdAsyncTask extends android.os.AsyncTask<Integer, Void,
            Void> {
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
}
