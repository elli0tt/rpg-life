package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

public class UpdateQuestsListUseCase {
    private QuestsRepository repository;

    public UpdateQuestsListUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(List<Quest> questsList){
        repository.insert(questsList);
    }
}
