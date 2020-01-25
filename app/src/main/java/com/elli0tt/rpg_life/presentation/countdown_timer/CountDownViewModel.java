package com.elli0tt.rpg_life.presentation.countdown_timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class CountDownViewModel extends ViewModel {
    private boolean isTimerRunning = false;
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>(START_TIME_LEFT_IN_MILLIS);
    private long endTime = 0;

    private static final long START_TIME_LEFT_IN_MILLIS = 60000L;

    private static final String ERROR_TIME = "-1:-1";

    public boolean isTimerRunning(){
        return isTimerRunning;
    }

    public LiveData<Long> getTimeLeftMillis(){
        return timeLeftMillis;
    }

    public void updateTimeLeftInMillis(long value){
        timeLeftMillis.setValue(value);
    }

    public String getTimeLeft(){
        if (timeLeftMillis.getValue() != null){
            long timeLeftInSeconds = timeLeftMillis.getValue() / 1000;
            long hours = timeLeftInSeconds / 3600;
            timeLeftInSeconds %= 3600;
            long minutes = timeLeftInSeconds / 60;
            long seconds = timeLeftInSeconds % 60;

            return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return ERROR_TIME;
        }
    }

    public void startTimer(long currentTimeMillis){
        isTimerRunning = true;
        if (endTime != 0){
            timeLeftMillis.setValue(endTime - currentTimeMillis);
        }
        endTime = currentTimeMillis + timeLeftMillis.getValue();
}

    public void pauseTimer(){
        isTimerRunning = false;
    }

    public void resetTimer(){
        timeLeftMillis.setValue(START_TIME_LEFT_IN_MILLIS);
        endTime = 0;
    }
}
