package com.elli0tt.rpg_life.domain.use_case.quests.load_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;

public class GetFilterStateUseCase {
    private QuestsRepository repository;

    public GetFilterStateUseCase(QuestsRepository repository){
        this.repository = repository;
    }

    public QuestsFilterState invoke(){
        return repository.getQuestsFilterState();
    }
}
