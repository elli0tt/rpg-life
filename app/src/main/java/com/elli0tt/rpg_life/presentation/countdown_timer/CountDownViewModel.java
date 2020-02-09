package com.elli0tt.rpg_life.presentation.countdown_timer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.CountDownTimerRepositoryImpl;
import com.elli0tt.rpg_life.domain.use_case.TimerUseCase;

public class CountDownViewModel extends AndroidViewModel {

    private MutableLiveData<TimerState> timerState = new MutableLiveData<>();
    private MutableLiveData<Long> timeLeftSeconds = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimerNew = new MutableLiveData<>(true);
    private long endTime;
    private long timerLengthSeconds;

    //NumberPickers current values
    private MutableLiveData<Integer> hours = new MutableLiveData<>(0);
    private MutableLiveData<Integer> minutes = new MutableLiveData<>(0);
    private MutableLiveData<Integer> seconds = new MutableLiveData<>(0);

    private CountDownTimerRepositoryImpl repository;

    public MutableLiveData<Integer> getHours() {
        return hours;
    }

    public MutableLiveData<Integer> getMinutes() {
        return minutes;
    }

    public MutableLiveData<Integer> getSeconds() {
        return seconds;
    }

    private static final String ERROR_TIME = "-1:-1:-1";

    public CountDownViewModel(@NonNull Application application) {
        super(application);
        repository = new CountDownTimerRepositoryImpl(application);

        timeLeftSeconds.setValue(repository.getTimeLeftSeconds());
        endTime = repository.getEndTime();
        timerState.setValue(repository.getTimerState());
        timerLengthSeconds = repository.getTimerLengthSeconds();
        isTimerNew.setValue(repository.getIsTimerNew());
    }

    LiveData<TimerState> getTimerState() {
        return timerState;
    }

    LiveData<Long> getTimeLeftSeconds() {
        return timeLeftSeconds;
    }

    LiveData<Boolean> isTimerNew() {
        return isTimerNew;
    }

    long getTimeLeftMillis() {
        return TimerUseCase.getTimeMillis(timeLeftSeconds.getValue());
    }

    void updateTimeLeftSeconds() {
        timeLeftSeconds.setValue(timeLeftSeconds.getValue() - 1);
    }

    String getTimeLeft() {
        if (timeLeftSeconds.getValue() != null) {
            return TimerUseCase.getTimeFormatted(timeLeftSeconds.getValue());
        } else {
            return ERROR_TIME;
        }
    }

    void startTimer(long currentTimeMillis) {
        timerState.setValue(TimerState.RUNNING);
        if (isTimerNew.getValue()) {
            timerLengthSeconds = TimerUseCase.getTimeSeconds(hours.getValue(), minutes.getValue()
                    , seconds.getValue());
            timeLeftSeconds.setValue(timerLengthSeconds);
            isTimerNew.setValue(false);
        }
        if (endTime != 0) {
            timeLeftSeconds.setValue(TimerUseCase.getTimeSeconds(endTime - currentTimeMillis));
        }
        if (timeLeftSeconds.getValue() < 0) {
            timeLeftSeconds.setValue(0L);
        }
        endTime = currentTimeMillis + TimerUseCase.getTimeMillis(timeLeftSeconds.getValue());
    }

    void pauseTimer() {
        timerState.setValue(TimerState.PAUSED);
        endTime = 0;
    }

    void stopTimer() {
        timerState.setValue(TimerState.STOPPED);
        timeLeftSeconds.setValue(0L);
        isTimerNew.setValue(true);
        endTime = 0;
    }

    void saveData() {
        repository.setTimerData(timeLeftSeconds.getValue(), endTime, timerState.getValue(),
                timerLengthSeconds, isTimerNew.getValue());
    }

    boolean isNeedToEnableStartFab() {
        return !(hours.getValue() == 0 && minutes.getValue() == 0 && seconds.getValue() == 0);
    }

    int getProgress() {
        return timeLeftSeconds.getValue().intValue();
    }

    int getMaxProgress() {
        return (int) timerLengthSeconds;
    }
}
