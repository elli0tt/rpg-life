package com.elli0tt.rpg_life.presentation.countdown_timer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.CountDownRepository;
import com.elli0tt.rpg_life.domain.utils.TimerUtils;

import java.util.Locale;

public class CountDownViewModel extends AndroidViewModel {

    public enum TimerState{
        RUNNING, PAUSED, STOPPED
    }

    private MutableLiveData<TimerState> timerState = new MutableLiveData<>();
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimerNew = new MutableLiveData<>(true);
    private long endTime;

    //NumberPickers current values
    private int hours;
    private int minutes;
    private int seconds;

    private CountDownRepository repository;

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    private static final long START_TIME_LEFT_IN_MILLIS = 60000L;

    private static final String ERROR_TIME = "-1:-1:-1";

    public CountDownViewModel(@NonNull Application application) {
        super(application);
        repository = new CountDownRepository(application);

        timeLeftMillis.setValue(repository.getTimeLeftMillis());
        endTime = repository.getEndTime();
        timerState.setValue(repository.getTimerState());
    }

    LiveData<TimerState> getTimerState(){
        return timerState;
    }

    LiveData<Long> getTimeLeftMillis(){
        return timeLeftMillis;
    }

    LiveData<Boolean> isTimerNew(){
        return isTimerNew;
    }

    void updateTimeLeftInMillis(long value){
        timeLeftMillis.setValue(value);
    }

    String getTimeLeft(){
        if (timeLeftMillis.getValue() != null){
            return String.format(Locale.getDefault(),"%02d:%02d:%02d",
                    TimerUtils.getHours(timeLeftMillis.getValue()),
                    TimerUtils.getMinutes(timeLeftMillis.getValue()),
                    TimerUtils.getSeconds(timeLeftMillis.getValue()));
        } else {
            return ERROR_TIME;
        }
    }

    void startTimer(long currentTimeMillis){
        timerState.setValue(TimerState.RUNNING);
        if (isTimerNew.getValue()){
            timeLeftMillis.setValue(TimerUtils.getTimeMillis(hours, minutes, seconds));
            isTimerNew.setValue(false);
        }
        if (endTime != 0){
            timeLeftMillis.setValue(endTime - currentTimeMillis);
        }
        if (timeLeftMillis.getValue() < 0){
            timeLeftMillis.setValue(0L);
        }
        endTime = currentTimeMillis + timeLeftMillis.getValue();
}

    void pauseTimer(){
        timerState.setValue(TimerState.PAUSED);
        endTime = 0;
    }

    void stopTimer(){
        timerState.setValue(TimerState.STOPPED);
        isTimerNew.setValue(true);
        endTime = 0;
    }

    void saveData(){
        repository.setTimerData(timeLeftMillis.getValue(), endTime, timerState.getValue());
    }
}
