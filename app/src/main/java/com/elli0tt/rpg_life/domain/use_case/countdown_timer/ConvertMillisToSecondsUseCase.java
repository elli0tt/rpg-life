package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

public class ConvertMillisToSecondsUseCase {
    public long invoke(long millis){
        return millis / Constants.MILLISECONDS_IN_SECOND;
    }
}
