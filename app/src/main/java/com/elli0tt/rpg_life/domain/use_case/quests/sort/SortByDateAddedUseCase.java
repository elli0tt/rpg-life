package com.elli0tt.rpg_life.domain.use_case.quests.sort;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Collections;
import java.util.List;

public class SortByDateAddedUseCase {
    public List<Quest> invoke(List<Quest> quests) {
        Collections.sort(quests, (quest1, quest2) -> Integer.compare(quest1.getId(),
                quest2.getId()));
        return quests;
    }
}
