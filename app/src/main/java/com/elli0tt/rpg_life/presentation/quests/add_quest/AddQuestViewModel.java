package com.elli0tt.rpg_life.presentation.quests.add_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

public class AddQuestViewModel extends AndroidViewModel {

    private QuestsRepositoryImpl repository;

    public AddQuestViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
    }

    public void saveQuest(@NonNull String name,
                          @NonNull String description,
                          @Quest.Difficulty int difficulty) {
        repository.insert(new Quest(name, description, difficulty));
    }


}
