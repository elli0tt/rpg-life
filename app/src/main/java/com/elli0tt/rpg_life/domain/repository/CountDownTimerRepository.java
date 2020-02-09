package com.elli0tt.rpg_life.domain.repository;

import com.elli0tt.rpg_life.presentation.countdown_timer.TimerState;

public interface CountDownTimerRepository {
    long getTimeLeftSeconds();

    long getEndTime();

    TimerState getTimerState();

    long getTimerLengthSeconds();

    boolean getIsTimerNew();

    void setTimerData(long timeLeftSeconds, long endTime,
                      TimerState timerState, long timerLengthSeconds,
                      boolean isTimerNew);

}
