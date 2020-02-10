package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Calendar;

public class GetQuestDateDueStateUseCase {
    public Quest.DateDueState invoke(Calendar dateDue) {
        Calendar currentDate = Calendar.getInstance();
        if (currentDate.after(dateDue)) {
            return Quest.DateDueState.RED;
        }
        return Quest.DateDueState.GREEN;
    }
}
