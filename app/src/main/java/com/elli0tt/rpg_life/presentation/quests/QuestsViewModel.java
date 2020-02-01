package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.use_case.QuestsUseCase;

import java.util.ArrayList;
import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private QuestsRepositoryImpl repository;

    private LiveData<List<Quest>> quests;

    private MutableLiveData<QuestsFilterType> currentFilter =
            new MutableLiveData<>(QuestsFilterType.ALL);

    public QuestsViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
        quests = Transformations.switchMap(currentFilter, new Function<QuestsFilterType,
                LiveData<List<Quest>>>() {
            @Override
            public LiveData<List<Quest>> apply(QuestsFilterType filterType) {
                switch (currentFilter.getValue()) {
                    case ALL:
                        return repository.getAllQuests();
                    case ACTIVE:
                        return repository.getActiveQuests();
                    case COMPLETED:
                        return repository.getCompletedQuests();
                    default:
                        return null;

                }
            }
        });
    }

    LiveData<List<Quest>> getQuests() {
        return quests;
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

    void setFiltering(QuestsFilterType filterType) {
        currentFilter.postValue(filterType);
    }
}
