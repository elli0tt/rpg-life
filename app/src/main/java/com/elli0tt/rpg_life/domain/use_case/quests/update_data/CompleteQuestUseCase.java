package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.constants.Constants;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetRelatedSkillsIdsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.UpdateSkillTotalXpByIdUseCase;

import java.util.Calendar;
import java.util.List;

public class CompleteQuestUseCase {
    private UpdateQuestsUseCase updateQuestsUseCase;
    private InsertQuestsUseCase insertQuestsUseCase;
    private UpdateSkillTotalXpByIdUseCase updateSkillTotalXpByIdUseCase;
    private GetRelatedSkillsIdsUseCase getRelatedSkillsIdsUseCase;
    private InsertRelatedSkillUseCase insertRelatedSkillUseCase;

    public CompleteQuestUseCase(QuestsRepository questsRepository, SkillsRepository skillsRepository) {
        updateQuestsUseCase = new UpdateQuestsUseCase(questsRepository);
        insertQuestsUseCase = new InsertQuestsUseCase(questsRepository);
        updateSkillTotalXpByIdUseCase = new UpdateSkillTotalXpByIdUseCase(skillsRepository);
        getRelatedSkillsIdsUseCase = new GetRelatedSkillsIdsUseCase(questsRepository);
        insertRelatedSkillUseCase = new InsertRelatedSkillUseCase(questsRepository);
    }

    public void invoke(Quest quest, boolean isCompleted) {
        quest.setCompleted(isCompleted);
        if (quest.isChallenge()){
            if (isCompleted){
                increaseRelatedSkillsXps(quest.getId(), quest.getDifficulty().getXpIncrease() + 10 * quest.getDayNumber());
                quest.setDayNumber(quest.getDayNumber() + 1);
                updateQuestsUseCase.invoke(quest);
                if (quest.getDayNumber() < quest.getTotalDaysCount()){
                    Quest newQuest = new Quest(quest.getName());
                    newQuest.setDifficulty(quest.getDifficulty());
                    newQuest.setCompleted(false);
                    newQuest.setDayNumber(quest.getDayNumber());
                    newQuest.setChallenge(true);
                    newQuest.setTotalDaysCount(quest.getTotalDaysCount());
                    insertQuestsUseCase.invoke(newQuest);
                }
            }
        } else {
            if (isCompleted) {
                increaseRelatedSkillsXps(quest.getId(), quest.getDifficulty().getXpIncrease());
            }
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
            }
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

    private void increaseRelatedSkillsXps(int questId, int xpIncrease){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<RelatedToQuestSkills> relatedSkills = getRelatedSkillsIdsUseCase.invoke(questId);
                for (RelatedToQuestSkills skill : relatedSkills){
                    updateSkillTotalXpByIdUseCase.invoke(skill.getSkillId(), xpIncrease * skill.getXpPercentage() / 100);
                }
            }
        }.start();
    }
}
