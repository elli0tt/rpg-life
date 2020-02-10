package com.elli0tt.rpg_life.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.data.shared_prefs.CountDownSharedPrefUtils;
import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.presentation.countdown_timer.TimerState;

public class CountDownTimerRepositoryImpl implements CountDownTimerRepository {
    private CountDownSharedPrefUtils countDownSharedPrefUtils;

    public CountDownTimerRepositoryImpl(@NonNull Context context) {
        countDownSharedPrefUtils = new CountDownSharedPrefUtils(context);
    }

    @Override
    public long getTimeLeftSeconds() {
        return countDownSharedPrefUtils.getTimeLeftSeconds();
    }

    @Override
    public long getEndTime() {
        return countDownSharedPrefUtils.getEndTime();
    }

    @Override
    public TimerState getTimerState() {
        return countDownSharedPrefUtils.getTimerState();
    }

    @Override
    public long getTimerLengthSeconds() {
        return countDownSharedPrefUtils.getTimerLengthSeconds();
    }

    @Override
    public boolean getIsTimerNew() {
        return countDownSharedPrefUtils.getIsTimerNew();
    }

    @Override
    public void setTimeLeftSeconds(long timeLeftSeconds) {
        countDownSharedPrefUtils.setTimeLeftSeconds(timeLeftSeconds);
    }

    @Override
    public void setEndTime(long endTime) {
        countDownSharedPrefUtils.setEndTime(endTime);
    }

    @Override
    public void setTimerState(TimerState timerState) {
        countDownSharedPrefUtils.setTimerState(timerState);
    }

    @Override
    public void setTimerLengthSeconds(long timerLengthSeconds) {
        countDownSharedPrefUtils.setTimerLengthSeconds(timerLengthSeconds);
    }

    @Override
    public void setIsTimerNew(boolean isTimerNew) {
        countDownSharedPrefUtils.setIsTimerNew(isTimerNew);
    }
}
