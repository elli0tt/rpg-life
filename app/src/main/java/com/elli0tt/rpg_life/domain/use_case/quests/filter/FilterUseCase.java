package com.elli0tt.rpg_life.domain.use_case.quests.filter;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;

import java.util.ArrayList;
import java.util.List;

public class FilterUseCase {
    public List<Quest> invoke(List<Quest> questsToFilter, QuestsFilterState filterState,
                              boolean isShowCompleted) {
        switch (filterState) {
            case ALL:
                return filterByAll(questsToFilter, isShowCompleted);
            case IMPORTANT:
                return filterByImportant(questsToFilter, isShowCompleted);
        }
        return questsToFilter;
    }

    private List<Quest> filterByAll(List<Quest> questsToFilter, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getAllWithCompleted(questsToFilter);
        }
        return getAllWithoutCompleted(questsToFilter);
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

    private List<Quest> filterByImportant(List<Quest> questsToFilter, boolean isShowCompleted) {
        if (isShowCompleted) {
            return getImportantWithCompleted(questsToFilter);
        }
        return getImportantWithoutCompleted(questsToFilter);
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
}
