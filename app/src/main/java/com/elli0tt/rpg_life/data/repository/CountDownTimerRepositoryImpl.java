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

    public long getTimeLeftSeconds() {
        return countDownSharedPrefUtils.getTimeLeftSeconds();
    }

    public long getEndTime() {
        return countDownSharedPrefUtils.getEndTime();
    }

    public TimerState getTimerState() {
        return countDownSharedPrefUtils.getTimerState();
    }

    public long getTimerLengthSeconds() {
        return countDownSharedPrefUtils.getTimerLengthSeconds();
    }

    public boolean getIsTimerNew() {
        return countDownSharedPrefUtils.getIsTimerNew();
    }

    public void setTimerData(long timeLeftSeconds, long endTime,
                             TimerState timerState, long timerLengthSeconds,
                             boolean isTimerNew) {
        countDownSharedPrefUtils.setTimerData(timeLeftSeconds, endTime, timerState,
                timerLengthSeconds, isTimerNew);
    }
}
