package com.elli0tt.rpg_life.presentation.skills;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.skills.DeleteAllSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.GetAllSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.InsertSkillsUseCase;

import java.util.ArrayList;
import java.util.List;

public class SkillsViewModel extends AndroidViewModel {
    private LiveData<List<Skill>> allSkills;

    private GetAllSkillsUseCase getAllSkillsUseCase;
    private InsertSkillsUseCase insertSkillsUseCase;
    private DeleteAllSkillsUseCase deleteAllSkillsUseCase;

    public SkillsViewModel(@NonNull Application application) {
        super(application);
        SkillsRepository skillsRepository = new SkillsRepositoryImpl(application);

        getAllSkillsUseCase = new GetAllSkillsUseCase(skillsRepository);
        insertSkillsUseCase = new InsertSkillsUseCase(skillsRepository);
        deleteAllSkillsUseCase = new DeleteAllSkillsUseCase(skillsRepository);

        allSkills = getAllSkillsUseCase.invoke();
    }

    LiveData<List<Skill>> getAllSkills() {
        return allSkills;
    }

    void populateWithSamples() {
        insertSkillsUseCase.invoke(generateSampleSkillsList().toArray(new Skill[0]));
    }

    private List<Skill> generateSampleSkillsList() {
        List<Skill> resultList = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            resultList.add(new Skill("Skill " + i));
        }
        return resultList;
    }

    void deleteAll() {
        deleteAllSkillsUseCase.invoke();
    }

}
