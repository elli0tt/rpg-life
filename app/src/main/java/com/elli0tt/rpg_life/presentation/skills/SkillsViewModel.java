package com.elli0tt.rpg_life.presentation.skills;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class SkillsViewModel extends AndroidViewModel {
    private SkillsRepository repository;

    private LiveData<List<Skill>> allSkills;

    public SkillsViewModel(@NonNull Application application) {
        super(application);
        repository = new SkillsRepository(application);
        allSkills = repository.getAllSkills();
    }

    public LiveData<List<Skill>> getAllSkills(){
        return allSkills;
    }

    void populateWithSamples(){
        repository.insert(generateSampleSkillsList());
    }

    private List<Skill> generateSampleSkillsList(){
        List<Skill> resultList = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i){
            resultList.add(new Skill("Skill " + i));
        }
        return resultList;
    }

    void deleteAll(){
        repository.deleteAll();
    }

}
