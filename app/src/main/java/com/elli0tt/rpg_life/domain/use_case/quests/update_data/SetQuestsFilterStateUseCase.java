package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;

public class SetQuestsFilterStateUseCase {
    private QuestsRepository repository;

    public SetQuestsFilterStateUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(QuestsFilterState questsFilterState){
        repository.setQuestsFilterState(questsFilterState);
    }
}
