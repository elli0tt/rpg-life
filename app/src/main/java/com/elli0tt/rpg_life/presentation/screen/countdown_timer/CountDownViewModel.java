package com.elli0tt.rpg_life.presentation.screen.countdown_timer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.CountDownTimerRepositoryImpl;
import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertMillisToSecondsUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertSecondsToMillisUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertToSecondsUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.GetTimeFormattedUseCase;

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

    private static final String ERROR_TIME = "-1:-1:-1";

    private ConvertMillisToSecondsUseCase convertMillisToSecondsUseCase;
    private ConvertSecondsToMillisUseCase convertSecondsToMillisUseCase;
    private ConvertToSecondsUseCase convertToSecondsUseCase;
    private GetTimeFormattedUseCase getTimeFormattedUseCase;

    private CountDownTimerRepository countDownTimerRepository;

    public CountDownViewModel(@NonNull Application application) {
        super(application);

        convertMillisToSecondsUseCase = new ConvertMillisToSecondsUseCase();
        convertSecondsToMillisUseCase = new ConvertSecondsToMillisUseCase();
        convertToSecondsUseCase = new ConvertToSecondsUseCase();
        getTimeFormattedUseCase = new GetTimeFormattedUseCase();

        countDownTimerRepository = new CountDownTimerRepositoryImpl(application);

        timeLeftSeconds.setValue(countDownTimerRepository.getTimeLeftSeconds());
        endTime = countDownTimerRepository.getEndTime();
        timerState.setValue(countDownTimerRepository.getTimerState());
        timerLengthSeconds = countDownTimerRepository.getTimerLengthSeconds();
        isTimerNew.setValue(countDownTimerRepository.getIsTimerNew());
    }

    public MutableLiveData<Integer> getHours() {
        return hours;
    }

    public MutableLiveData<Integer> getMinutes() {
        return minutes;
    }

    public MutableLiveData<Integer> getSeconds() {
        return seconds;
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
        return convertSecondsToMillisUseCase.invoke(timeLeftSeconds.getValue());
    }

    void updateTimeLeftSeconds() {
        timeLeftSeconds.setValue(timeLeftSeconds.getValue() - 1);
    }

    String getTimeLeft() {
        return getTimeFormattedUseCase.invoke(timeLeftSeconds.getValue());
    }

    void startTimer(long currentTimeMillis) {
        timerState.setValue(TimerState.RUNNING);
        if (isTimerNew.getValue()) {
            timerLengthSeconds = convertToSecondsUseCase.invoke(hours.getValue(), minutes.getValue()
                    , seconds.getValue());
            timeLeftSeconds.setValue(timerLengthSeconds);
            isTimerNew.setValue(false);
        }
        if (endTime != 0) {
            timeLeftSeconds.setValue(convertMillisToSecondsUseCase.invoke(endTime - currentTimeMillis));
        }
        if (timeLeftSeconds.getValue() < 0) {
            timeLeftSeconds.setValue(0L);
        }
        endTime =
                currentTimeMillis + convertSecondsToMillisUseCase.invoke(timeLeftSeconds.getValue());
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
        countDownTimerRepository.setEndTime(endTime);
        countDownTimerRepository.setIsTimerNew(isTimerNew.getValue());
        countDownTimerRepository.setTimeLeftSeconds(timeLeftSeconds.getValue());
        countDownTimerRepository.setTimerLengthSeconds(timerLengthSeconds);
        countDownTimerRepository.setTimerState(timerState.getValue());
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
