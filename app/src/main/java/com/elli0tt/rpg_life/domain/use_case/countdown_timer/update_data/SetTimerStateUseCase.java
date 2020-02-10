package com.elli0tt.rpg_life.domain.use_case.countdown_timer.update_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.presentation.countdown_timer.TimerState;

public class SetTimerStateUseCase {
    private CountDownTimerRepository repository;

    public SetTimerStateUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public void invoke(TimerState timerState){
        repository.setTimerState(timerState);
    }
}
