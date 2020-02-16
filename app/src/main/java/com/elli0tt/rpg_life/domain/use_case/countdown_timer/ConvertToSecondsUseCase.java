package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

public class ConvertToSecondsUseCase {
    public long invoke(int hours, int minutes, int seconds){
        return (hours * Constants.SECONDS_IN_HOUR + minutes * Constants.SECONDS_IN_MINUTE + seconds);
    }
}
