package com.elli0tt.rpg_life.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.elli0tt.rpg_life.data.shared_prefs.SharedPreferencesUtils;
import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.presentation.screen.countdown_timer.TimerState;

public class CountDownTimerRepositoryImpl implements CountDownTimerRepository {
    private SharedPreferencesUtils sharedPreferencesUtils;

    public CountDownTimerRepositoryImpl(@NonNull Context context) {
        sharedPreferencesUtils = new SharedPreferencesUtils(context);
    }

    @Override
    public long getTimeLeftSeconds() {
        return sharedPreferencesUtils.getTimeLeftSeconds();
    }

    @Override
    public long getEndTime() {
        return sharedPreferencesUtils.getEndTime();
    }

    @Override
    public TimerState getTimerState() {
        return sharedPreferencesUtils.getTimerState();
    }

    @Override
    public long getTimerLengthSeconds() {
        return sharedPreferencesUtils.getTimerLengthSeconds();
    }

    @Override
    public boolean getIsTimerNew() {
        return sharedPreferencesUtils.isTimerNew();
    }

    @Override
    public void setTimeLeftSeconds(long timeLeftSeconds) {
        sharedPreferencesUtils.setTimeLeftSeconds(timeLeftSeconds);
    }

    @Override
    public void setEndTime(long endTime) {
        sharedPreferencesUtils.setEndTime(endTime);
    }

    @Override
    public void setTimerState(TimerState timerState) {
        sharedPreferencesUtils.setTimerState(timerState);
    }

    @Override
    public void setTimerLengthSeconds(long timerLengthSeconds) {
        sharedPreferencesUtils.setTimerLengthSeconds(timerLengthSeconds);
    }

    @Override
    public void setIsTimerNew(boolean isTimerNew) {
        sharedPreferencesUtils.setTimerNew(isTimerNew);
    }
}
