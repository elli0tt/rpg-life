package com.elli0tt.rpg_life.domain.use_case.quests;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Collections;
import java.util.List;

public class QuestsSortByNameUseCase {
    public List<Quest> invoke(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> quest1.getName().compareTo(quest2.getName()));
        return quests;
    }
}
