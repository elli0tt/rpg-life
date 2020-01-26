package com.elli0tt.rpg_life.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.dao.SkillsDao;
import com.elli0tt.rpg_life.data.database.room_database.AppRoomDatabase;
import com.elli0tt.rpg_life.domain.model.Skill;

import java.util.List;

public class SkillsRepository {
    private SkillsDao skillsDao;

    public SkillsRepository(Application application){
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        skillsDao = database.getSkillsDao();
    }

    public void insert(Skill skill){
        new InsertAsyncTask(skillsDao).execute(skill);
    }

    private static class InsertAsyncTask extends AsyncTask<Skill, Void, Void>{
        private SkillsDao dao;

        public InsertAsyncTask(SkillsDao dao){
            dao = this.dao;
        }

        @Override
        protected Void doInBackground(Skill... skills) {
            dao.insert(skills[0]);
            return null;
        }
    }

    public void insert(List<Skill> skillList){
        new InsertListAsyncTask(skillsDao).execute(skillList);
    }

    private static class InsertListAsyncTask extends AsyncTask<List<Skill>, Void, Void>{
        private SkillsDao dao;

        public InsertListAsyncTask(SkillsDao dao){
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Skill>... lists) {
            dao.insert(lists[0]);
            return null;
        }
    }

    public LiveData<List<Skill>> getAllSkills(){
        return skillsDao.getAllSkills();
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(skillsDao).execute();
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private SkillsDao dao;

        public DeleteAllAsyncTask(SkillsDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

}
