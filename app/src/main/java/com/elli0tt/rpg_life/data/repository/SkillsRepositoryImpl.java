package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.domain.model.AddSkillData;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

import java.util.List;

public class SkillsRepositoryImpl implements SkillsRepository {
    private SkillsDao skillsDao;

    public SkillsRepositoryImpl(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        skillsDao = database.getSkillsDao();
    }

    @Override
    public void insert(Skill... skills) {
        new InsertAsyncTask(skillsDao).execute(skills);
    }

    private static class InsertAsyncTask extends android.os.AsyncTask<Skill, Void, Void> {
        private SkillsDao dao;

        InsertAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.insert(skills);
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
    public void update(Skill... skills){
        new UpdateAsyncTask(skillsDao).execute(skills);
    }

    private static class UpdateAsyncTask extends AsyncTask<Skill, Void, Void> {
        private SkillsDao dao;

        UpdateAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.update(skills);
            return null;
        }
    }

    @Override
    public void updateTotalXpById(int id, long totalXp){
        new UpdateTotalXpByIdAsyncTask(skillsDao).execute(new Pair<>(id, totalXp));
    }

    private static class UpdateTotalXpByIdAsyncTask extends AsyncTask<Pair<Integer, Long>, Void, Void> {
        private SkillsDao dao;

        UpdateTotalXpByIdAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Pair<Integer, Long>... params) {
            dao.updateTotalXpById(params[0].first, params[0].second);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        new DeleteAllAsyncTask(skillsDao).execute();
    }

    private static class DeleteAllAsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        private SkillsDao dao;

        DeleteAllAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

}
