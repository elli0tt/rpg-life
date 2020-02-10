package com.elli0tt.rpg_life.domain.use_case.countdown_timer.load_data;

import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;

public class GetTimerLengthSecondsUseCase {
    private CountDownTimerRepository repository;

    public GetTimerLengthSecondsUseCase(CountDownTimerRepository repository) {
        this.repository = repository;
    }

    public long invoke() {
        return repository.getTimerLengthSeconds();
    }
}
