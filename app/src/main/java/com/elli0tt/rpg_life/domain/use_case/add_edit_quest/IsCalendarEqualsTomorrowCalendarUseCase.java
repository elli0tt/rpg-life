package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class IsCalendarEqualsTomorrowCalendarUseCase {
    public boolean invoke(Calendar calendar) {
        Calendar tomorrowCalendar = new GetTomorrowCalendarUseCase().invoke();
        return tomorrowCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                tomorrowCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                tomorrowCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                tomorrowCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY) &&
                tomorrowCalendar.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE);
    }
}
