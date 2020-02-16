package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

public class GetSecondsInSecondsUseCase {
    public long  invoke(long seconds){
        return seconds % Constants.SECONDS_IN_HOUR % Constants.SECONDS_IN_MINUTE;
    }
}
