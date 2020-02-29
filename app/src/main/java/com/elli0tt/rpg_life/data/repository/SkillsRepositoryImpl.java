package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
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

    private static class InsertAsyncTask extends AsyncTask<Skill, Void, Void> {
        private SkillsDao dao;

        InsertAsyncTask(SkillsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.insert(skills[0]);
            return null;
        }
    }

    @Override
    public LiveData<List<Skill>> getAllSkills() {
        return skillsDao.getAllSkills();
    }

    @Override
    public void deleteAll() {
        new DeleteAllAsyncTask(skillsDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
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
