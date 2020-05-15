package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import com.elli0tt.rpg_life.domain.constants.Constants;

public class ConvertMillisToSecondsUseCase {

    public long invoke(long millis){
        return millis / Constants.MILLISECONDS_IN_SECOND;
    }
}
