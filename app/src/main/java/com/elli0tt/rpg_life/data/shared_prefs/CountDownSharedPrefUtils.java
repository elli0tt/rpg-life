package com.elli0tt.rpg_life.data.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class CountDownSharedPrefUtils {

    private SharedPreferences sharedPreferences;

    private static final String SHARED_PREFERENCES_NAME = "countdown shared preferences";
    private static final String KEY_TIME_LEFT_MILLIS = "time left millis";
    private static final String KEY_END_TIME = "end time";
    private static final String KEY_IS_TIMER_RUNNING = "is timer running";

    public CountDownSharedPrefUtils(@NonNull Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    public long getTimeLeftMillis(){
        return sharedPreferences.getLong(KEY_TIME_LEFT_MILLIS, 60000L);
    }

    public long getEndTime(){
        return sharedPreferences.getLong(KEY_END_TIME, 0);
    }

    public boolean isTimerRunning(){
        return sharedPreferences.getBoolean(KEY_IS_TIMER_RUNNING, false);
    }

    public void setTimerData(long timeLeftMillis, long endTime, boolean isTimerRunning){
        sharedPreferences.edit()
                .putLong(KEY_TIME_LEFT_MILLIS, timeLeftMillis)
                .putLong(KEY_END_TIME, endTime)
                .putBoolean(KEY_IS_TIMER_RUNNING, isTimerRunning)
                .apply();
    }


}
