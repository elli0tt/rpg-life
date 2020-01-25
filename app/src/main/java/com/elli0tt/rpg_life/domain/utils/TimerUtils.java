package com.elli0tt.rpg_life.domain.utils;

public class TimerUtils {
    public static long getHours(long timeInMillis){
        return timeInMillis / 1000 / 3600;
    }

    public static long getMinutes(long timeInMillis){
        return timeInMillis / 1000 % 3600 / 60;
    }

    public static long getSeconds(long timeInMillis){
        return timeInMillis / 1000 % 3600 % 60;
    }
}
