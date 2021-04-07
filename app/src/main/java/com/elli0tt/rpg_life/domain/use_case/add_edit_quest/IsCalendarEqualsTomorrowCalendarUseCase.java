package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

import javax.inject.Inject;

public class IsCalendarEqualsTomorrowCalendarUseCase {

    @Inject
    public IsCalendarEqualsTomorrowCalendarUseCase() {
        //do nothing
    }

    public boolean invoke(Calendar calendar) {
        Calendar tomorrowCalendar = new GetTomorrowCalendarUseCase().invoke();
        return tomorrowCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                tomorrowCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                tomorrowCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }
}
