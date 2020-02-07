package com.elli0tt.rpg_life.domain.use_case;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestsSortByDateAddedUseCase {
    public static List<Quest> sort(List<Quest> quests){
        Collections.sort(quests, new Comparator<Quest>() {
            @Override
            public int compare(Quest quest1, Quest quest2) {
                return Integer.compare(quest1.getId(), quest2.getId());
            }
        });
        return quests;
    }
}
