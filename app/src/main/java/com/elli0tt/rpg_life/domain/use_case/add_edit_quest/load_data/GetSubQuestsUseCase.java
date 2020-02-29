package com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

public class GetSubQuestsUseCase {
    private QuestsRepository repository;

    public GetSubQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Quest>> invoke(int parentQuestId){
        return repository.getSubQuests(parentQuestId);
    }
}
