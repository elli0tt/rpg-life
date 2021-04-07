package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

import javax.inject.Inject;

public class IsCalendarEqualsTodayCalendarUseCase {

    @Inject
    public IsCalendarEqualsTodayCalendarUseCase() {
        //do nothing
    }

    public boolean invoke(Calendar calendar) {
        Calendar todayCalendar = new GetTodayCalendarUseCase().invoke();
        return todayCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                todayCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                todayCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }
}
