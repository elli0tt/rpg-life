package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class SetShowCompletedUseCase {
    private QuestsRepository repository;

    public SetShowCompletedUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke(boolean isShowCompleted){
        repository.setShowCompleted(isShowCompleted);
    }
}
