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
        Collections.sort(quests, (quest1, quest2) -> {
            if (quest1.id == quest2.id) {
                return quest1.name.compareTo(quest2.name);
            }
            return Integer.compare(quest1.id, quest2.id);
        });
        return quests;
    }

    private List<Quest> sortByDateDue(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> {
            if (quest1.dateDue.equals(quest2.dateDue)) {
                return quest1.name.compareTo(quest2.name);
            }
            return quest1.dateDue.compareTo(quest2.dateDue);
        });
        return quests;
    }

    private List<Quest> sortByDifficulty(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> {
            if (quest1.difficulty == quest2.difficulty) {
                return quest1.name.compareTo(quest2.name);
            }
            return Integer.compare(quest1.difficulty.ordinal(), quest2.difficulty.ordinal());
        });
        return quests;
    }

    private List<Quest> sortByName(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> quest1.name.compareTo(quest2.name));
        return quests;
    }
}
