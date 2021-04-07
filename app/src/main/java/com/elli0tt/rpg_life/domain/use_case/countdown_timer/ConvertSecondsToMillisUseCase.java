package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

import javax.inject.Inject;

public class ConvertSecondsToMillisUseCase {

    @Inject
    public ConvertSecondsToMillisUseCase() {
        //do nothing
    }

    public long invoke(long seconds) {
        return seconds * Constants.MILLISECONDS_IN_SECOND;
    }
}
