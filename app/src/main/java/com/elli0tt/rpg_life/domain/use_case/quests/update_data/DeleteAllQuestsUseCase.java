package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class DeleteAllQuestsUseCase {
    private QuestsRepository repository;

    public DeleteAllQuestsUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.deleteAll();
    }
}
