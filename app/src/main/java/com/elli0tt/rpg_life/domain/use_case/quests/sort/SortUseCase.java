package com.elli0tt.rpg_life.domain.use_case.quests.sort;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

import java.util.Collections;
import java.util.List;

public class SortUseCase {
    public List<Quest> invoke(List<Quest> quests, QuestsSortingState sortingState) {
        switch (sortingState) {
            case NAME:
                return sortByName(quests);
            case DATE_DUE:
                return sortByDateDue(quests);
            case DATE_ADDED:
                return sortByDateAdded(quests);
            case DIFFICULTY:
                return sortByDifficulty(quests);
        }
        return quests;
    }

    private List<Quest> sortByDateAdded(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> Integer.compare(quest1.getId(),
                quest2.getId()));
        return quests;
    }

    private List<Quest> sortByDateDue(List<Quest> quests) {
        Collections.sort(quests,
                (quest1, quest2) -> quest1.getDateDue().compareTo(quest2.getDateDue()));
        return quests;
    }

    private List<Quest> sortByDifficulty(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> Integer.compare(quest1.getDifficulty(),
                quest2.getDifficulty()));
        return quests;
    }

    private List<Quest> sortByName(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> quest1.getName().compareTo(quest2.getName()));
        return quests;
    }
}