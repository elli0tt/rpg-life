package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class UpdateQuestsUseCase {
    private QuestsRepository repository;

    public UpdateQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Quest... quests){
        repository.update(quests);
    }
}
