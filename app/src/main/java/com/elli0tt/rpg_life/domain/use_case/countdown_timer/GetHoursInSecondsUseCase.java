package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

public class GetHoursInSecondsUseCase {
    public long invoke(long seconds){
        return seconds / Constants.SECONDS_IN_HOUR;
    }
}
