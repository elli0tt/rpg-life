package com.elli0tt.rpg_life.data.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.presentation.countdown_timer.TimerState;

public class CountDownSharedPrefUtils {

    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFERENCES_NAME = "countdown shared preferences";
    private static final String KEY_TIME_LEFT_SECONDS = "time left seconds";
    private static final String KEY_END_TIME = "end time";
    private static final String KEY_TIMER_STATE = "is timer running";
    private static final String KEY_TIMER_LENGTH_SECONDS = "timer length seconds";
    private static final String KEY_IS_TIMER_NEW = "is timer new";

    public CountDownSharedPrefUtils(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    public long getTimeLeftSeconds() {
        return sharedPreferences.getLong(KEY_TIME_LEFT_SECONDS, 60000L);
    }

    public long getEndTime() {
        return sharedPreferences.getLong(KEY_END_TIME, 0);
    }

    public TimerState getTimerState() {
        int ordinal = sharedPreferences.getInt(KEY_TIMER_STATE, TimerState.STOPPED.ordinal());
        return TimerState.values()[ordinal];
    }

    public long getTimerLengthSeconds() {
        return sharedPreferences.getLong(KEY_TIMER_LENGTH_SECONDS, 0);
    }

    public boolean getIsTimerNew() {
        return sharedPreferences.getBoolean(KEY_IS_TIMER_NEW, true);
    }

    public void setTimeLeftSeconds(long timeLeftSeconds) {
        sharedPreferences.edit().putLong(KEY_TIME_LEFT_SECONDS, timeLeftSeconds).apply();
    }

    public void setEndTime(long endTime) {
        sharedPreferences.edit().putLong(KEY_END_TIME, endTime).apply();
    }

    public void setTimerState(TimerState timerState) {
        sharedPreferences.edit().putInt(KEY_TIMER_STATE, timerState.ordinal()).apply();
    }

    public void setTimerLengthSeconds(long timerLengthSeconds){
        sharedPreferences.edit().putLong(KEY_TIMER_LENGTH_SECONDS, timerLengthSeconds).apply();
    }

    public void setIsTimerNew(boolean isTimerNew){
        sharedPreferences.edit().putBoolean(KEY_IS_TIMER_NEW, isTimerNew).apply();
    }
}
