package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private QuestsRepositoryImpl repository;
    private LiveData<List<Quest>> allQuestsList;

    public QuestsViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
        allQuestsList = repository.getAllQuestsList();
    }

    public LiveData<List<Quest>> getAllQuestsList(){
        return allQuestsList;
    }

    public void insert(List<Quest> questList){
        repository.insert(questList);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void update(Quest quest){
        repository.update(quest);
    }

}
