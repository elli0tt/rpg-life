package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

import java.util.List;

public interface QuestsRepository {
    Quest getQuestById(int id);

    LiveData<List<Quest>> getAllQuests();

    LiveData<List<Quest>> getActiveQuests();

    LiveData<List<Quest>> getCompletedQuests();

    LiveData<List<Quest>> getImportantQuests();

    void insert(Quest quest);

    void insert(List<Quest> questList);

    void update(Quest quest);

    void delete(List<Quest> questList);

    void deleteAll();

    QuestsFilterState getQuestsFilterState();

    void setQuestsFilterState(QuestsFilterState filterState);

    QuestsSortingState getQuestSortingState();

    void setQuestsSoringState(QuestsSortingState sortingState);

}
