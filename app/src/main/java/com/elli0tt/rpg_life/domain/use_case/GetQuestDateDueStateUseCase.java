package com.elli0tt.rpg_life.domain.use_case;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Calendar;

public class GetQuestDateDueStateUseCase {
    public Quest.DateDueState invoke(Calendar dateDue){
        Calendar currentDate = Calendar.getInstance();
        if (currentDate.after(dateDue)){
            return Quest.DateDueState.RED;
        }
        final int MILLIS_IN_24_HOURS = 24 * 3600 * 1000;
        if (dateDue.getTimeInMillis() - currentDate.getTimeInMillis() < MILLIS_IN_24_HOURS){
            return Quest.DateDueState.YELLOW;
        }
        return Quest.DateDueState.GREEN;
    }
}
