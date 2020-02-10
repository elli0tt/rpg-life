package com.elli0tt.rpg_life.domain.use_case.countdown_timer.load_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.presentation.countdown_timer.TimerState;

public class GetTimerStateUseCase {
    private CountDownTimerRepository repository;

    public GetTimerStateUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public TimerState invoke() {
        return repository.getTimerState();
    }
}
