package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class TimePickerDialogUseCase {
    public static int getCurrentHour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
}
