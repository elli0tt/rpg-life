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
    private boolean isTimerRunning;
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>();
    private long endTime;

    private CountDownRepository countDownRepository;

    private static final long START_TIME_LEFT_IN_MILLIS = 60000L;
    public static final int COUNT_DOWN_INTERVAL = 1000;

    private static final String ERROR_TIME = "-1:-1:-1";

    public CountDownViewModel(@NonNull Application application) {
        super(application);
        countDownRepository = new CountDownRepository(application);

        timeLeftMillis.setValue(countDownRepository.getTimeLeftMillis());
        endTime = countDownRepository.getEndTime();
        isTimerRunning = countDownRepository.isTimerRunning();
    }

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
            return String.format(Locale.getDefault(),"%02d:%02d:%02d",
                    TimerUtils.getHours(timeLeftMillis.getValue()),
                    TimerUtils.getMinutes(timeLeftMillis.getValue()),
                    TimerUtils.getSeconds(timeLeftMillis.getValue()));
        } else {
            return ERROR_TIME;
        }
    }

    public void startTimer(long currentTimeMillis){
        isTimerRunning = true;
        if (endTime != 0){
            timeLeftMillis.setValue(endTime - currentTimeMillis);
        }
        if (timeLeftMillis.getValue() < 0){
            timeLeftMillis.setValue(0L);
        }
        endTime = currentTimeMillis + timeLeftMillis.getValue();
}

    public void pauseTimer(){
        isTimerRunning = false;
        endTime = 0;
    }

    public void resetTimer(){
        timeLeftMillis.setValue(START_TIME_LEFT_IN_MILLIS);
        endTime = 0;
    }

    public void saveData(){
        countDownRepository.setTimerData(timeLeftMillis.getValue(), endTime, isTimerRunning);
    }
}
