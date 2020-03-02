package com.elli0tt.rpg_life.domain.use_case.quests.load_data;

import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class GetShowCompletedUseCase {
    QuestsRepository repository;

    public GetShowCompletedUseCase(QuestsRepository repository) {
        this.repository = repository;
    }

    public boolean invoke(){
        return repository.isShowCompleted();
    }
}
