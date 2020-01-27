package com.elli0tt.rpg_life.domain.utils;

public class TimerUtils {
    private static final int SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLISECONDS_IN_SECOND = 1000;

    public static long getHours(long timeInMillis) {
        return timeInMillis / MILLISECONDS_IN_SECOND / SECONDS_IN_HOUR;
    }

    public static long getMinutes(long timeInMillis) {
        return timeInMillis / MILLISECONDS_IN_SECOND % SECONDS_IN_HOUR / SECONDS_IN_MINUTE;
    }

    public static long getSeconds(long timeInMillis) {
        return timeInMillis / MILLISECONDS_IN_SECOND % SECONDS_IN_HOUR % SECONDS_IN_MINUTE;
    }

    public static long getTimeMillis(int hours, int minutes, int seconds) {
        return (hours * SECONDS_IN_HOUR + minutes * SECONDS_IN_MINUTE + seconds) * MILLISECONDS_IN_SECOND;
    }
}
