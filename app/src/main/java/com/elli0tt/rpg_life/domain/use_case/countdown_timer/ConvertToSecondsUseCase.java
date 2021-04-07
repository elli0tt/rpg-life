package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

import javax.inject.Inject;

public class ConvertToSecondsUseCase {

    @Inject
    public ConvertToSecondsUseCase() {
        //do nothing
    }

    public long invoke(int hours, int minutes, int seconds) {
        return (hours * Constants.SECONDS_IN_HOUR + minutes * Constants.SECONDS_IN_MINUTE + seconds);
    }
}
