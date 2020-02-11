package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Calendar;

public class GetQuestDateDueStateUseCase {
    private IsCalendarEqualsTodayCalendarUseCase isCalendarEqualsTodayCalendarUseCase;
    private IsCalendarEqualsTomorrowCalendarUseCase isCalendarEqualsTomorrowCalendarUseCase;

    public GetQuestDateDueStateUseCase(){
        isCalendarEqualsTodayCalendarUseCase = new IsCalendarEqualsTodayCalendarUseCase();
        isCalendarEqualsTomorrowCalendarUseCase = new IsCalendarEqualsTomorrowCalendarUseCase();
    }

    public Quest.DateDueState invoke(Calendar dateDue) {
        Calendar currentDate = Calendar.getInstance();
        if (isCalendarEqualsTodayCalendarUseCase.invoke(currentDate)){
            return Quest.DateDueState.TODAY;
        }
        if  (isCalendarEqualsTomorrowCalendarUseCase.invoke(currentDate)){
            return Quest.DateDueState.TOMORROW;
        }
        if (currentDate.after(dateDue)) {
            return Quest.DateDueState.AFTER_DATE_DUE;
        }
        return Quest.DateDueState.BEFORE_DATE_DUE;
    }
}
