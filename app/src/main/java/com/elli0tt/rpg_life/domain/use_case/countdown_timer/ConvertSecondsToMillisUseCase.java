package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

public class ConvertSecondsToMillisUseCase {
    public long invoke(long seconds){
        return seconds * Constants.MILLISECONDS_IN_SECOND;
    }
}
