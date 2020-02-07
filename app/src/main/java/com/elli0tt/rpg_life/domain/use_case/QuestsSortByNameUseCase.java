package com.elli0tt.rpg_life.domain.use_case;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestsSortByNameUseCase {
    public static List<Quest> sort(List<Quest> quests){
        Collections.sort(quests, new Comparator<Quest>() {
            @Override
            public int compare(Quest quest1, Quest quest2) {
                return quest1.getName().compareTo(quest2.getName());
            }
        });
        return quests;
    }
}
