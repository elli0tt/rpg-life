package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

public class ConvertSecondsToMillisUseCase {
    public long invoke(long seconds) {
        return seconds * Constants.MILLISECONDS_IN_SECOND;
    }
}
