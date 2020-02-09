package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class GetNextWeekCalendarUseCase {
    public Calendar invoke(){
        Calendar calendar = Calendar.getInstance();
        final int MILLIS_IN_7_DAYS = 1000 * 3600 * 24 * 7;
        calendar.setTimeInMillis(calendar.getTimeInMillis() + MILLIS_IN_7_DAYS);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return calendar;
    }
}
