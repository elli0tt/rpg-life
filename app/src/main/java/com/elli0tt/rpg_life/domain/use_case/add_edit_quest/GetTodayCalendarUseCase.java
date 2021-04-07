package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

import javax.inject.Inject;

public class GetTodayCalendarUseCase {

    @Inject
    public GetTodayCalendarUseCase() {
        //do nothing
    }

    public Calendar invoke() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return calendar;
    }
}
