package com.elli0tt.rpg_life.domain.use_case;

import com.elli0tt.rpg_life.domain.model.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsUseCase {
    public static List<Quest> getActiveQuests(List<Quest> quests){
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests){
            if (!quest.isCompleted()){
                resultList.add(quest);
            }
        }
        return resultList;
    }

    public static List<Quest> getCompletedQuest(List<Quest> quests){
        List<Quest> resultList = new ArrayList<>();
        for (Quest quest : quests){
            if (quest.isCompleted()){
                resultList.add(quest);
            }
        }
        return resultList;
    }
}
