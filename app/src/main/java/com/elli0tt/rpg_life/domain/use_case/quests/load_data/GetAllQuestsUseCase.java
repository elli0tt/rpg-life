package com.elli0tt.rpg_life.domain.use_case.quests.load_data;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

public class GetAllQuestsUseCase {
    private QuestsRepository repository;

    public GetAllQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Quest>> invoke(){
        return repository.getAllQuests();
    }
}
