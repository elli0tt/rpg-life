package com.elli0tt.rpg_life.domain.use_case.countdown_timer.update_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class SetTimeLeftSecondsUseCase {
    private CountDownTimerRepository repository;

    public SetTimeLeftSecondsUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public void invoke(long timeLeftSeconds) {
        repository.setTimeLeftSeconds(timeLeftSeconds);
    }
}
