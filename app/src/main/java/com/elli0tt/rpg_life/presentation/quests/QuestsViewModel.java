package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.ArrayList;
import java.util.LinkedList;
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

    private List<Quest> generateSampleQuestsList() {
        List<Quest> result = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            result.add(new Quest("Quest " + i));
        }
        return result;
    }

    public void populateWithSamples(){
        repository.insert(generateSampleQuestsList());
    }

    @Nullable
    public List<Long> getAllIds() {
        List<Quest> allQuests = allQuestsList.getValue();
        if (allQuests != null) {
            List<Long> result = new ArrayList<>(allQuests.size());
            for (Quest quest : allQuests){
                result.add((long)quest.getId());
            }
            return result;
        }
        return null;
    }

    public List<Long> getAllKeys(){
        List<Quest> allQuests = allQuestsList.getValue();
        if (allQuests != null) {
            List<Long> result = new ArrayList<>(allQuests.size());
            for (long i = 0; i < allQuests.size(); ++i){
                result.add(i);
            }
            return result;
        }
        return null;
    }

}
