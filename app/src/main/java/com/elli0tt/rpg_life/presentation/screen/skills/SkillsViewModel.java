package com.elli0tt.rpg_life.presentation.screen.skills;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.model.SkillsSortingState;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.skills.SortSkillsUseCase;
import com.elli0tt.rpg_life.presentation.worker.InsertEmptySkillWorker;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SkillsViewModel extends ViewModel {

    private final LiveData<List<Skill>> allSkills;
    private final MutableLiveData<SkillsSortingState> sortingState = new MutableLiveData<>();
    private final MediatorLiveData<List<Skill>> skillsToShow = new MediatorLiveData<>();
    private final LiveData<Integer> sortedByTextResId = Transformations.map(sortingState,
            this::getSortedByTextResId);

    private final SkillsRepository skillsRepository;

    private final WorkManager workManager;

    private WorkRequest insertEmptySkillWorkRequest;

    @Inject
    public SkillsViewModel(
            SortSkillsUseCase sortSkillsUseCase,
            SkillsRepository skillsRepository,
            WorkManager workManager
    ) {
        this.skillsRepository = skillsRepository;
        this.workManager = workManager;

        updateInsertEmptySkillWorkRequest();

        sortingState.setValue(skillsRepository.getSkillsSortingState());
        allSkills = skillsRepository.getAllSkills();

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

    LiveData<SkillsSortingState> getSortingState() {
        return sortingState;
    }

    void setSortingState(SkillsSortingState sortingState) {
        this.sortingState.setValue(sortingState);
        skillsRepository.setSkillsSortingState(sortingState);
    }

    LiveData<Integer> getSortedByTextResId() {
        return sortedByTextResId;
    }

    LiveData<WorkInfo> getInsertEmptySkillWorkInfo() {
        return workManager.getWorkInfoByIdLiveData(insertEmptySkillWorkRequest.getId());
    }

    void populateWithSamples() {
        skillsRepository.insertSkills(generateSampleSkillsList().toArray(new Skill[0]));
    }

    private List<Skill> generateSampleSkillsList() {
        List<Skill> resultList = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            resultList.add(new Skill(0, "Skill " + i));
        }
        return resultList;
    }

    void deleteAll() {
        skillsRepository.deleteAllSkills();
    }

    void changeSortingDirection() {
        SkillsSortingState skillsSortingState = sortingState.getValue();
        if (skillsSortingState == null) {
            skillsSortingState = SkillsSortingState.NAME_ASC;
        }
        switch (skillsSortingState) {
            case NAME_ASC:
                setSortingState(SkillsSortingState.NAME_DESC);
                break;
            case NAME_DESC:
                setSortingState(SkillsSortingState.NAME_ASC);
                break;
            case LEVEL_ASC:
                setSortingState(SkillsSortingState.LEVEL_DESC);
                break;
            case LEVEL_DESC:
                setSortingState(SkillsSortingState.LEVEL_ASC);
                break;
        }

    }

    private int getSortedByTextResId(SkillsSortingState skillsSortingState) {
        SkillsSortingState sortingState = skillsSortingState;
        if (sortingState == null) {
            sortingState = SkillsSortingState.NAME_ASC;
        }
        switch (sortingState) {
            case NAME_ASC:
            case NAME_DESC:
                return R.string.skills_sorted_by_name;
            case LEVEL_ASC:
            case LEVEL_DESC:
                return R.string.skills_sorted_by_level;
        }
        return 0;
    }

    int getSkillId(int position) {
        if (skillsToShow.getValue() == null) {
            return Quest.DEFAULT_ID;
        }
        return skillsToShow.getValue().get(position).getId();
    }

    void insertEmptySkill() {
        workManager.enqueue(insertEmptySkillWorkRequest);
    }

    void updateInsertEmptySkillWorkRequest() {
        insertEmptySkillWorkRequest =
                new OneTimeWorkRequest.Builder(InsertEmptySkillWorker.class).build();
    }
}