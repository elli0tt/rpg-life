package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class GetTomorrowCalendarUseCase {
    public Calendar invoke() {
        Calendar calendar = Calendar.getInstance();
        final int MILLIS_IN_24_HOURS = 1000 * 3600 * 24;
        calendar.setTimeInMillis(calendar.getTimeInMillis() + MILLIS_IN_24_HOURS);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return calendar;
    }
}
