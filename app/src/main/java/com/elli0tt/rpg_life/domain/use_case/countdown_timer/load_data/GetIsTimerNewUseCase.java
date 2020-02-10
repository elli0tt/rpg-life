package com.elli0tt.rpg_life.domain.use_case.countdown_timer.load_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class GetIsTimerNewUseCase {
    private CountDownTimerRepository repository;

    public GetIsTimerNewUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public boolean invoke() {
        return repository.getIsTimerNew();
    }
}
