package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

import javax.inject.Inject;

public class GetSecondsInSecondsUseCase {

    @Inject
    public GetSecondsInSecondsUseCase() {
        //do nothing
    }

    public long invoke(long seconds) {
        return seconds % Constants.SECONDS_IN_HOUR % Constants.SECONDS_IN_MINUTE;
    }
}
