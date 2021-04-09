package com.elli0tt.rpg_life.domain.repository;

import com.elli0tt.rpg_life.domain.model.TimerState;

public interface CountDownTimerRepository {
    long getTimeLeftSeconds();

    void setTimeLeftSeconds(long timeLeftSeconds);

    long getEndTime();

    void setEndTime(long endTime);

    TimerState getTimerState();

    void setTimerState(TimerState timerState);

    long getTimerLengthSeconds();

    void setTimerLengthSeconds(long timerLengthSeconds);

    boolean getIsTimerNew();

    void setIsTimerNew(boolean isTimerNew);
}
