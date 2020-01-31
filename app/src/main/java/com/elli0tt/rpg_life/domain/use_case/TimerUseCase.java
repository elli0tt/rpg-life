package com.elli0tt.rpg_life.domain.use_case;

import java.util.Locale;

public class TimerUseCase {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLISECONDS_IN_SECOND = 1000;

    public static String getTimeFormatted(long timeSeconds){
        return String.format(Locale.getDefault(),"%02d:%02d:%02d",
                TimerUseCase.getHours(timeSeconds),
                TimerUseCase.getMinutes(timeSeconds),
                TimerUseCase.getSeconds(timeSeconds));
    }

    private static long getHours(long timeSeconds) {
        return timeSeconds / SECONDS_IN_HOUR;
    }

    private static long getMinutes(long timeSeconds) {
        return timeSeconds % SECONDS_IN_HOUR / SECONDS_IN_MINUTE;
    }

    private static long getSeconds(long timeInSeconds) {
        return timeInSeconds % SECONDS_IN_HOUR % SECONDS_IN_MINUTE;
    }

    public static long getTimeSeconds(int hours, int minutes, int seconds) {
        return (hours * SECONDS_IN_HOUR + minutes * SECONDS_IN_MINUTE + seconds);
    }

    public static long getTimeMillis(long seconds){
        return seconds * MILLISECONDS_IN_SECOND;
    }
    
    public static long getTimeSeconds(long millis){
        return millis / MILLISECONDS_IN_SECOND;
    }
}
