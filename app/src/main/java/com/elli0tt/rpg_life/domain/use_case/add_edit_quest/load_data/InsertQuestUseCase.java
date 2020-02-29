package com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class InsertQuestUseCase {
    private QuestsRepository repository;

    public InsertQuestUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(Quest quest){
        repository.insert(quest);
    }
}
