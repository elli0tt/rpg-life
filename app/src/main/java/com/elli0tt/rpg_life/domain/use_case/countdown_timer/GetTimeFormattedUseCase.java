package com.elli0tt.rpg_life.domain.use_case.countdown_timer;

import java.util.Locale;

import javax.inject.Inject;

public class GetTimeFormattedUseCase {

    @Inject
    public GetTimeFormattedUseCase() {
        //do nothing
    }

    public String invoke(long seconds) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                new GetHoursInSecondsUseCase().invoke(seconds),
                new GetMinutesInSecondsUseCase().invoke(seconds),
                new GetSecondsInSecondsUseCase().invoke(seconds));
    }
}
