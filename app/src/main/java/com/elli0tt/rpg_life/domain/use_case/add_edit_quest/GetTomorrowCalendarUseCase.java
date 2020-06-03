package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.domain.constants.Constants;

import java.util.Calendar;

public class GetTomorrowCalendarUseCase {
    public Calendar invoke() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis() + Constants.MILLIS_IN_24_HOURS);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return calendar;
    }
}
