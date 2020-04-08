package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.constants.Constants;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.UpdateSkillTotalXpByIdUseCase;

import java.util.Calendar;

public class CompleteQuestUseCase {
    private UpdateQuestsUseCase updateQuestsUseCase;
    private InsertQuestsUseCase insertQuestsUseCase;
    private UpdateSkillTotalXpByIdUseCase updateSkillTotalXpByIdUseCase;

    public CompleteQuestUseCase(QuestsRepository questsRepository, SkillsRepository skillsRepository) {
        updateQuestsUseCase = new UpdateQuestsUseCase(questsRepository);
        insertQuestsUseCase = new InsertQuestsUseCase(questsRepository);
        updateSkillTotalXpByIdUseCase = new UpdateSkillTotalXpByIdUseCase(skillsRepository);
    }

    public void invoke(Quest quest, boolean isCompleted) {
        quest.setCompleted(isCompleted);
        increaseRelatedSkillsXps(quest);
        updateQuestsUseCase.invoke(quest);
        if (!quest.getRepeatState().equals(Quest.RepeatState.NOT_SET)) {
            Quest newQuest = new Quest(quest.getName());
            //newQuest.name = quest.name;
            newQuest.setDescription(quest.getDescription());
            newQuest.setDifficulty(quest.getDifficulty());
            newQuest.setCompleted(false);
            newQuest.setRepeatState(quest.getRepeatState());
            newQuest.setDateDueSet(quest.isDateDueSet());
            newQuest.setDateDue(calculateNewDateDue(quest.getDateDue(), quest.getRepeatState()));
            newQuest.setImportant(quest.isImportant());
            insertQuestsUseCase.invoke(newQuest);
            increaseRelatedSkillsXps(quest);
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

    private void increaseRelatedSkillsXps(Quest quest){
        for (int id : quest.getRelatedSkillsIds()){
            updateSkillTotalXpByIdUseCase.invoke(id, quest.getDifficulty().getXpIncrease());
        }
    }
}
