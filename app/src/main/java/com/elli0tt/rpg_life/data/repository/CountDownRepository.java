package com.elli0tt.rpg_life.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.data.shared_prefs.CountDownSharedPrefUtils;

public class CountDownRepository {
    CountDownSharedPrefUtils countDownSharedPrefUtils;

    public CountDownRepository(@NonNull Context context){
        countDownSharedPrefUtils = new CountDownSharedPrefUtils(context);
    }

    public long getTimeLeftMillis(){
        return countDownSharedPrefUtils.getTimeLeftMillis();
    }

    public long getEndTime(){
        return countDownSharedPrefUtils.getEndTime();
    }

    public boolean isTimerRunning(){
        return countDownSharedPrefUtils.isTimerRunning();
    }

    public void setTimerData(long timeLeftMillis, long endTime, boolean isTimerRunning){
        countDownSharedPrefUtils.setTimerData(timeLeftMillis, endTime, isTimerRunning);
    }
}
