package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class GetCurrentDayOfMonthUseCase {
    public int invoke() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
}
