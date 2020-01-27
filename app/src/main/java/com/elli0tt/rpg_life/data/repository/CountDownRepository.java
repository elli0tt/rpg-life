package com.elli0tt.rpg_life.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.data.shared_prefs.CountDownSharedPrefUtils;
import com.elli0tt.rpg_life.presentation.countdown_timer.CountDownViewModel;

public class CountDownRepository {
    private CountDownSharedPrefUtils countDownSharedPrefUtils;

    public CountDownRepository(@NonNull Context context){
        countDownSharedPrefUtils = new CountDownSharedPrefUtils(context);
    }

    public long getTimeLeftMillis(){
        return countDownSharedPrefUtils.getTimeLeftMillis();
    }

    public long getEndTime(){
        return countDownSharedPrefUtils.getEndTime();
    }

    public CountDownViewModel.TimerState getTimerState(){
        return countDownSharedPrefUtils.getTimerState();
    }

    public void setTimerData(long timeLeftMillis, long endTime, CountDownViewModel.TimerState timerState){
        countDownSharedPrefUtils.setTimerData(timeLeftMillis, endTime, timerState);
    }
}
