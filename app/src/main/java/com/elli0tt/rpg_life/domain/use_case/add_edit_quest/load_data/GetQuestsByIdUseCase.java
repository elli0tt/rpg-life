package com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.List;

public class GetQuestsByIdUseCase {
    private QuestsRepository repository;

    public GetQuestsByIdUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public List<Quest> invoke(List<Integer> ids){
        return repository.getQuestsByIds(ids);
    }
}
