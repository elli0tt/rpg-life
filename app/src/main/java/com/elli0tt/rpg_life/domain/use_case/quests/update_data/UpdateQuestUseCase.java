package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class UpdateQuestUseCase {
    private QuestsRepository repository;

    public UpdateQuestUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Quest quest){
        repository.update(quest);
    }
}
