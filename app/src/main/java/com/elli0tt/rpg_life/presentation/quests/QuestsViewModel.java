package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.use_case.quests.QuestsSortByDateAddedUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.QuestsSortByDateDueUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.QuestsSortByDiffucultyUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.QuestsSortByNameUseCase;

import java.util.ArrayList;
import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private QuestsRepositoryImpl repository;

    private LiveData<List<Quest>> quests;

    private MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();

    private MediatorLiveData questsToShow = new MediatorLiveData<>();

    private MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();

    private QuestsSortByNameUseCase questsSortByNameUseCase = new QuestsSortByNameUseCase();
    private QuestsSortByDateAddedUseCase questsSortByDateAddedUseCase =
            new QuestsSortByDateAddedUseCase();
    private QuestsSortByDateDueUseCase questsSortByDateDueUseCase =
            new QuestsSortByDateDueUseCase();
    private QuestsSortByDiffucultyUseCase questsSortByDiffucultyUseCase =
            new QuestsSortByDiffucultyUseCase();

    public QuestsViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
        currentFilterState.setValue(repository.getQuestsFilterState());
        currentSortingState.setValue(repository.getQuestSortingState());

        quests = Transformations.switchMap(currentFilterState, new Function<QuestsFilterState,
                LiveData<List<Quest>>>() {
            @Override
            public LiveData<List<Quest>> apply(QuestsFilterState filterType) {
                switch (currentFilterState.getValue()) {
                    case ALL:
                        return repository.getAllQuests();
                    case ACTIVE:
                        return repository.getActiveQuests();
                    case COMPLETED:
                        return repository.getCompletedQuests();
                    case IMPORTANT:
                        return repository.getImportantQuests();
                    default:
                        return null;

                }
            }
        });

        questsToShow.addSource(currentSortingState, new Observer<QuestsSortingState>() {
            @Override
            public void onChanged(QuestsSortingState questsSortingState) {
                if (quests.getValue() != null) {
                    switch (currentSortingState.getValue()) {
                        case NAME:
                            questsToShow.setValue(questsSortByNameUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DATE_DUE:
                            questsToShow.setValue(questsSortByDateDueUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DATE_ADDED:
                            questsToShow.setValue(questsSortByDateAddedUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DIFFICULTY:
                            questsToShow.setValue(questsSortByDiffucultyUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                    }
                }
            }
        });
        questsToShow.addSource(quests, new Observer<List<Quest>>() {
            @Override
            public void onChanged(List<Quest> quests) {
                if (quests != null) {
                    switch (currentSortingState.getValue()) {
                        case NAME:
                            questsToShow.setValue(questsSortByNameUseCase.invoke(quests));
                            break;
                        case DATE_DUE:
                            questsToShow.setValue(questsSortByDateDueUseCase.invoke(quests));
                            break;
                        case DATE_ADDED:
                            questsToShow.setValue(questsSortByDateAddedUseCase.invoke(quests));
                            break;
                        case DIFFICULTY:
                            questsToShow.setValue(questsSortByDiffucultyUseCase.invoke(quests));
                            break;
                    }
                }
            }
        });
    }

    LiveData<List<Quest>> getQuests() {
        return questsToShow;
    }

    public void insert(List<Quest> questList) {
        repository.insert(questList);
    }

    void deleteAll() {
        repository.deleteAll();
    }

    void update(Quest quest) {
        repository.update(quest);
    }

    private List<Quest> generateSampleQuestsList() {
        List<Quest> result = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            result.add(new Quest("Quest " + i));
        }
        return result;
    }

    void populateWithSamples() {
        repository.insert(generateSampleQuestsList());
    }

    public void delete(List<Quest> questList) {
        repository.delete(questList);
    }

    void setFiltering(QuestsFilterState filterState) {
        currentFilterState.setValue(filterState);
        repository.setQuestsFilterState(filterState);
    }

    void setSorting(QuestsSortingState sortingState) {
        currentSortingState.setValue(sortingState);
        repository.setQuestsSoringState(sortingState);
    }
}
