package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import java.util.Calendar;

public class GetCurrentYearUseCase {
    public int invoke() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
