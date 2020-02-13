package com.elli0tt.rpg_life.domain.use_case.add_edit_quest;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;

public class GetRepeatTextResIdUseCase {
    public int invoke(Quest.RepeatState repeatState) {
        switch (repeatState) {
            case NOT_SET:
                return R.string.add_edit_quest_repeat;
            case DAILY:
                return R.string.add_edit_quest_repeat_popup_daily;
            case WEEKDAYS:
                return R.string.add_edit_quest_repeat_popup_weekdays;
            case WEEKENDS:
                return R.string.add_edit_quest_repeat_popup_weekends;
            case WEEKLY:
                return R.string.add_edit_quest_repeat_popup_weekly;
            case MONTHLY:
                return R.string.add_edit_quest_repeat_popup_monthly;
            case YEARLY:
                return R.string.add_edit_quest_repeat_popup_yearly;
            default:
                return 0;
        }
    }
}
