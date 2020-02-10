package com.elli0tt.rpg_life.domain.use_case.countdown_timer.update_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class SetTimerLengthSecondsUseCase {
    private CountDownTimerRepository repository;

    public SetTimerLengthSecondsUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public void invoke(long timerLengthSeconds){
        repository.setTimeLeftSeconds(timerLengthSeconds);
    }
}
