package com.elli0tt.rpg_life.domain.repository;

import com.elli0tt.rpg_life.presentation.screen.countdown_timer.TimerState;

public interface CountDownTimerRepository {
    long getTimeLeftSeconds();

    long getEndTime();

    TimerState getTimerState();

    long getTimerLengthSeconds();

    boolean getIsTimerNew();

    void setTimeLeftSeconds(long timeLeftSeconds);

    void setEndTime(long endTime);

    void setTimerState(TimerState timerState);

    void setTimerLengthSeconds(long timerLengthSeconds);

    void setIsTimerNew(boolean isTimerNew);
}
