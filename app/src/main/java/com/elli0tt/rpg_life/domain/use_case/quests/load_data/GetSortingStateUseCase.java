package com.elli0tt.rpg_life.domain.use_case.quests.load_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

public class GetSortingStateUseCase {
    private QuestsRepository repository;

    public GetSortingStateUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public QuestsSortingState invoke(){
        return repository.getQuestSortingState();
    }
}
