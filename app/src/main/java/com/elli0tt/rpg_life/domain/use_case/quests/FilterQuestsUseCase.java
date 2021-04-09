package com.elli0tt.rpg_life.domain.use_case.quests;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTomorrowCalendarUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FilterQuestsUseCase {
    private final IsCalendarEqualsTodayCalendarUseCase isCalendarEqualsTodayCalendarUseCase;
    private final IsCalendarEqualsTomorrowCalendarUseCase isCalendarEqualsTomorrowCalendarUseCase;

    @Inject
    public FilterQuestsUseCase(
            IsCalendarEqualsTodayCalendarUseCase isCalendarEqualsTodayCalendarUseCase,
            IsCalendarEqualsTomorrowCalendarUseCase isCalendarEqualsTomorrowCalendarUseCase
    ) {
        this.isCalendarEqualsTodayCalendarUseCase = isCalendarEqualsTodayCalendarUseCase;
        this.isCalendarEqualsTomorrowCalendarUseCase = isCalendarEqualsTomorrowCalendarUseCase;
    }

    public List<Quest> invoke(List<Quest> quests, QuestsFilterState filterState,
                              boolean isShowCompleted) {
        switch (filterState) {
            case ALL:
                return filterByAll(quests, isShowCompleted);
            case IMPORTANT:
                return filterByImportant(quests, isShowCompleted);
            case TODAY:
                return filterByToday(quests, isShowCompleted);
            case TOMORROW:
                return filterByTomorrow(quests, isShowCompleted);
        }
        return quests;
    }

    private List<Quest> filterByAll(List<Quest> quests, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getAllWithCompleted(quests);
        }
        return getAllWithoutCompleted(quests);
    }

    private List<Quest> getAllWithCompleted(List<Quest> quests) {
        return quests;
    }

    private List<Quest> getAllWithoutCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (!quest.isCompleted()) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> filterByImportant(List<Quest> quests, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getImportantWithCompleted(quests);
        }
        return getImportantWithoutCompleted(quests);
    }

    private List<Quest> getImportantWithCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.isImportant()) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> getImportantWithoutCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (quest.isImportant() && !quest.isCompleted()) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> filterByToday(List<Quest> quests, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getTodayWithCompleted(quests);
        }

        return getTodayWithoutCompleted(quests);
    }

    private List<Quest> getTodayWithCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (isCalendarEqualsTodayCalendarUseCase.invoke(quest.getDateDue())) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> getTodayWithoutCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (isCalendarEqualsTodayCalendarUseCase.invoke(quest.getDateDue())
                    && !quest.isCompleted()) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> filterByTomorrow(List<Quest> quests, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getTomorrowWithCompleted(quests);
        }
        return getTomorrowWithoutCompleted(quests);
    }

    private List<Quest> getTomorrowWithCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (isCalendarEqualsTomorrowCalendarUseCase.invoke(quest.getDateDue())) {
                resultList.add(quest);
            }
        }
        return resultList;
    }

    private List<Quest> getTomorrowWithoutCompleted(List<Quest> quests) {
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests) {
            if (isCalendarEqualsTomorrowCalendarUseCase.invoke(quest.getDateDue())
                    && !quest.isCompleted()) {
                resultList.add(quest);
            }
        }
        return resultList;
    }
}
