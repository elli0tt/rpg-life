package com.elli0tt.rpg_life.domain.use_case.countdown_timer.update_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class SetIsTimerNewUseCase {
    private CountDownTimerRepository repository;

    public SetIsTimerNewUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public void invoke(boolean isTimerNew){
        repository.setIsTimerNew(isTimerNew);
    }
}
