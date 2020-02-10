package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class GetQuestByIdUseCase {
    private QuestsRepository repository;

    public GetQuestByIdUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public Quest invoke(int id){
        return repository.getQuestById(id);
    }
}
