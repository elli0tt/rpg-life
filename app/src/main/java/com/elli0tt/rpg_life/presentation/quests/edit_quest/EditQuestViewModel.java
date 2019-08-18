package com.elli0tt.rpg_life.presentation.quests.edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

public class EditQuestViewModel extends AndroidViewModel {

    private QuestsRepositoryImpl repository;
    private final MutableLiveData<Quest> currentQuest = new MutableLiveData<>();

    public EditQuestViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
    }

    public void editQuest(@NonNull String name,
                          @NonNull String description,
                          @Quest.Difficulty int difficulty) {
        currentQuest.getValue().setName(name);
        currentQuest.getValue().setDescription(description);
        currentQuest.getValue().setDifficulty(difficulty);
        repository.update(currentQuest.getValue());
    }

    public void setCurrentQuest(Quest quest) {
        currentQuest.setValue(quest);
    }

    @NonNull
    public LiveData<Quest> getCurrentQuest() {
        return currentQuest;
    }
}
