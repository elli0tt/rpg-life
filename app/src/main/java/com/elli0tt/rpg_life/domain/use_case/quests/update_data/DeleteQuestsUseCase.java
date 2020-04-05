package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class DeleteQuestsUseCase {
    private QuestsRepository repository;

    public DeleteQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Quest... quests){
        repository.delete(quests);
    }
}
