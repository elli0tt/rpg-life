package com.elli0tt.rpg_life.domain.use_case.countdown_timer.update_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class SetEndTimeUseCase {
    private CountDownTimerRepository repository;

    public SetEndTimeUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public void invoke(long endTime){
        repository.setEndTime(endTime);
    }
}
