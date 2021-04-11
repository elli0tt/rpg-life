package com.elli0tt.rpg_life.presentation.screen.countdown_timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elli0tt.rpg_life.domain.model.TimerState;
import com.elli0tt.rpg_life.domain.repository.CountDownTimerRepository;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertMillisToSecondsUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertSecondsToMillisUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.ConvertToSecondsUseCase;
import com.elli0tt.rpg_life.domain.use_case.countdown_timer.GetTimeFormattedUseCase;

import javax.inject.Inject;

public class CountDownViewModel extends ViewModel {

    private final MutableLiveData<TimerState> timerState = new MutableLiveData<>();
    private final MutableLiveData<Long> timeLeftSeconds = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTimerNew = new MutableLiveData<>(true);
    //NumberPickers current values
    private final MutableLiveData<Integer> hours = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> minutes = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> seconds = new MutableLiveData<>(0);

    private final ConvertMillisToSecondsUseCase convertMillisToSecondsUseCase;
    private final ConvertSecondsToMillisUseCase convertSecondsToMillisUseCase;
    private final ConvertToSecondsUseCase convertToSecondsUseCase;
    private final GetTimeFormattedUseCase getTimeFormattedUseCase;
    private final CountDownTimerRepository countDownTimerRepository;

    private long endTime;
    private long timerLengthSeconds;

    @Inject
    public CountDownViewModel(
            ConvertMillisToSecondsUseCase convertMillisToSecondsUseCase,
            ConvertSecondsToMillisUseCase convertSecondsToMillisUseCase,
            ConvertToSecondsUseCase convertToSecondsUseCase,
            GetTimeFormattedUseCase getTimeFormattedUseCase,
            CountDownTimerRepository countDownTimerRepository
    ) {
        this.convertMillisToSecondsUseCase = convertMillisToSecondsUseCase;
        this.convertSecondsToMillisUseCase = convertSecondsToMillisUseCase;
        this.convertToSecondsUseCase = convertToSecondsUseCase;
        this.getTimeFormattedUseCase = getTimeFormattedUseCase;
        this.countDownTimerRepository = countDownTimerRepository;

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
        return convertSecondsToMillisUseCase.invoke(timeLeftSeconds.getValue() == null ? 0 :
                timeLeftSeconds.getValue());
    }

    void updateTimeLeftSeconds() {
        timeLeftSeconds.setValue(timeLeftSeconds.getValue() == null ? 0 :
                timeLeftSeconds.getValue() - 1);
    }

    String getTimeLeft() {
        return getTimeFormattedUseCase.invoke(timeLeftSeconds.getValue() == null ? 0 :
                timeLeftSeconds.getValue());
    }

    void startTimer(long currentTimeMillis) {
        timerState.setValue(TimerState.RUNNING);
        if (isTimerNew.getValue() != null && isTimerNew.getValue()) {
            timerLengthSeconds = convertToSecondsUseCase.invoke(
                    hours.getValue() == null ? 0 : hours.getValue(),
                    minutes.getValue() == null ? 0 : minutes.getValue(),
                    seconds.getValue() == null ? 0 : seconds.getValue());
            timeLeftSeconds.setValue(timerLengthSeconds);
            isTimerNew.setValue(false);
        }
        if (endTime != 0) {
            timeLeftSeconds.setValue(convertMillisToSecondsUseCase.invoke(endTime - currentTimeMillis));
        }
        if ((timeLeftSeconds.getValue() == null ? 0 : timeLeftSeconds.getValue()) < 0) {
            timeLeftSeconds.setValue(0L);
        }
        endTime =
                currentTimeMillis + convertSecondsToMillisUseCase.invoke(
                        timeLeftSeconds.getValue() == null ? 0 : timeLeftSeconds.getValue()
                );
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
        countDownTimerRepository.setIsTimerNew(isTimerNew.getValue() == null ? false :
                isTimerNew.getValue());
        countDownTimerRepository.setTimeLeftSeconds(timeLeftSeconds.getValue() == null ? 0 :
                timeLeftSeconds.getValue());
        countDownTimerRepository.setTimerLengthSeconds(timerLengthSeconds);
        countDownTimerRepository.setTimerState(timerState.getValue());
    }

    boolean isNeedToEnableStartFab() {
        return !((hours.getValue() == null || hours.getValue() == 0) &&
                (minutes.getValue() == null || minutes.getValue() == 0) &&
                (seconds.getValue() == null || seconds.getValue() == 0));
    }

    int getProgress() {
        return timeLeftSeconds.getValue() == null ? 0 : timeLeftSeconds.getValue().intValue();
    }

    int getMaxProgress() {
        return (int) timerLengthSeconds;
    }
}
