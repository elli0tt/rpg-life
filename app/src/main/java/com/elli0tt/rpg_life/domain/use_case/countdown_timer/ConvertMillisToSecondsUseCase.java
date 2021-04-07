package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

import javax.inject.Inject;

public class ConvertMillisToSecondsUseCase {

    @Inject
    public ConvertMillisToSecondsUseCase() {
        //do nothing
    }

    public long invoke(long millis) {
        return millis / Constants.MILLISECONDS_IN_SECOND;
    }
}
