package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class InsertQuestsUseCase {
    private QuestsRepository repository;

    public InsertQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Quest... quests){
        repository.insert(quests);
    }
}
