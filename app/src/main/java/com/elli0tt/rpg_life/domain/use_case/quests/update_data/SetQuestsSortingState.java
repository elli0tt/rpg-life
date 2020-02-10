package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

public class SetQuestsSortingState {
    private QuestsRepository repository;

    public SetQuestsSortingState(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(QuestsSortingState questsSortingState){
        repository.setQuestsSoringState(questsSortingState);
    }
}
