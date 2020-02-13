package com.elli0tt.rpg_life.domain.use_case.quests.load_data;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

public class GetImportantQuestsUseCase {
    private QuestsRepository repository;

    public GetImportantQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Quest>> invoke(){
        return repository.getImportantQuests();
    }
}