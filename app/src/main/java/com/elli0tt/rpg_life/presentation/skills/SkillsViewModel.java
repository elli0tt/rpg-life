package com.elli0tt.rpg_life.presentation.skills;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.skills.SortSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.load_data.GetAllSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.load_data.GetSkillsSortingStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.DeleteAllSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.InsertSkillsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.SetSkillsSortingStateUseCase;

import java.util.ArrayList;
import java.util.List;

public class SkillsViewModel extends AndroidViewModel {
    private LiveData<List<Skill>> allSkills;
    private MutableLiveData<SkillsSortingState> sortingState = new MutableLiveData<>();
    private MediatorLiveData<List<Skill>> skillsToShow = new MediatorLiveData<>();

    private GetAllSkillsUseCase getAllSkillsUseCase;
    private InsertSkillsUseCase insertSkillsUseCase;
    private DeleteAllSkillsUseCase deleteAllSkillsUseCase;
    private GetSkillsSortingStateUseCase getSkillsSortingStateUseCase;
    private SetSkillsSortingStateUseCase setSkillsSortingStateUseCase;

    private SortSkillsUseCase sortSkillsUseCase = new SortSkillsUseCase();

    public SkillsViewModel(@NonNull Application application) {
        super(application);
        SkillsRepository skillsRepository = new SkillsRepositoryImpl(application);

        getAllSkillsUseCase = new GetAllSkillsUseCase(skillsRepository);
        insertSkillsUseCase = new InsertSkillsUseCase(skillsRepository);
        deleteAllSkillsUseCase = new DeleteAllSkillsUseCase(skillsRepository);
        getSkillsSortingStateUseCase = new GetSkillsSortingStateUseCase(skillsRepository);
        setSkillsSortingStateUseCase = new SetSkillsSortingStateUseCase(skillsRepository);

        sortingState.setValue(getSkillsSortingStateUseCase.invoke());
        allSkills = getAllSkillsUseCase.invoke();

        skillsToShow.addSource(allSkills, skills -> {
            if (skills != null && sortingState.getValue() != null) {
                skillsToShow.setValue(sortSkillsUseCase.invoke(skills, sortingState.getValue()));
            }
        });

        skillsToShow.addSource(sortingState, skillsSortingState -> {
            if (allSkills.getValue() != null && skillsSortingState != null) {
                skillsToShow.setValue(sortSkillsUseCase.invoke(allSkills.getValue(),
                        skillsSortingState));
            }
        });
    }

    LiveData<List<Skill>> getSkillsToShow() {
        return skillsToShow;
    }

    void populateWithSamples() {
        insertSkillsUseCase.invoke(generateSampleSkillsList().toArray(new Skill[0]));
    }

    private List<Skill> generateSampleSkillsList() {
        List<Skill> resultList = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            resultList.add(new Skill(0, "Skill " + i));
        }
        return resultList;
    }

    void deleteAll() {
        deleteAllSkillsUseCase.invoke();
    }

    void setSortingState(SkillsSortingState sortingState){
        this.sortingState.setValue(sortingState);
        setSkillsSortingStateUseCase.invoke(sortingState);
    }

}
