package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.constants.Constants;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.InsertQuestUseCase;

import java.util.Calendar;

public class CompleteQuestUseCase {
    private UpdateQuestUseCase updateQuestUseCase;
    private InsertQuestUseCase insertQuestUseCase;

    public CompleteQuestUseCase(QuestsRepository repository) {
        updateQuestUseCase = new UpdateQuestUseCase(repository);
        insertQuestUseCase = new InsertQuestUseCase(repository);
    }

    public void invoke(Quest quest, boolean isCompleted) {
        quest.setCompleted(isCompleted);
        updateQuestUseCase.invoke(quest);
        if (!quest.getRepeatState().equals(Quest.RepeatState.NOT_SET)) {
            Quest newQuest = new Quest();
            newQuest.setName(quest.getName());
            newQuest.setDescription(quest.getDescription());
            newQuest.setDifficulty(quest.getDifficulty());
            newQuest.setCompleted(false);
            newQuest.setRepeatState(quest.getRepeatState());
            newQuest.setIsDateDueSet(quest.isDateDueSet());
            newQuest.setDateDue(calculateNewDateDue(quest.getDateDue(), quest.getRepeatState()));
            newQuest.setImportant(quest.isImportant());
            insertQuestUseCase.invoke(newQuest);
        }
    }

    private Calendar calculateNewDateDue(Calendar oldDateDue, Quest.RepeatState repeatState) {
        Calendar newDateDue = (Calendar) oldDateDue.clone();
        switch (repeatState) {
            case DAILY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis() + Constants.MILLIS_IN_24_HOURS);
                break;
            case WEEKDAYS:
                calculateNewDateDueForWeekdays(newDateDue);
                break;
            case WEEKENDS:
                calculateNewDateDueForWeekends(newDateDue);
                break;
            case WEEKLY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis() + Constants.MILLIS_IN_7_DAYS);
                break;
            case MONTHLY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis() + Constants.MILLIS_IN_30_DAYS);
                break;
            case YEARLY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis() + Constants.MILLIS_IN_365_DAYS);
                break;
        }
        return newDateDue;
    }

    private void calculateNewDateDueForWeekdays(Calendar newDateDue) {
        switch (newDateDue.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.FRIDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 3);
                break;
            case Calendar.SATURDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 2);
                break;
//                  Monday, Tuesday, Wednesday, Thursday, Sunday
            default:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS);
                break;
        }
    }

    private void calculateNewDateDueForWeekends(Calendar newDateDue) {
        switch (newDateDue.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 5);
                break;
            case Calendar.TUESDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 4);
                break;
            case Calendar.WEDNESDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 3);
                break;
            case Calendar.THURSDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 2);
                break;
            case Calendar.SUNDAY:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS * 6);
                break;
//            Friday, Saturday
            default:
                newDateDue.setTimeInMillis(newDateDue.getTimeInMillis()
                        + Constants.MILLIS_IN_24_HOURS);
                break;
        }
    }
}
