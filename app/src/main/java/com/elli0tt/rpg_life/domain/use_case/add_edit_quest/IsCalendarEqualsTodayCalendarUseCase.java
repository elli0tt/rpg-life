package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class IsCalendarEqualsTodayCalendarUseCase {
    public boolean invoke(Calendar calendar) {
        Calendar todayCalendar = new GetTodayCalendarUseCase().invoke();
        return todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                todayCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                todayCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY) &&
                todayCalendar.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE);
    }
}
