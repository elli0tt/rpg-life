package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.quests.QuestsSortingState;

import java.util.List;

public interface QuestsRepository {
    Quest getQuestById(int id);

    List<Quest> getQuestsByIds(List<Integer> ids);

    LiveData<List<Quest>> getAllQuests();

    LiveData<List<Quest>> getSubQuests(int parentQuestId);

    LiveData<Quest> getQuestByIdLiveData(int questId);

    void insert(Quest... quests);

    void update(Quest... quests);

    void updateQuestHasSubquestsById(int id, boolean hasSubquests);

    void delete(Quest... quests);

    void deleteAll();

    QuestsFilterState getQuestsFilterState();

    void setQuestsFilterState(QuestsFilterState filterState);

    QuestsSortingState getQuestSortingState();

    void setQuestsSoringState(QuestsSortingState sortingState);

    boolean isShowCompleted();

    void setShowCompleted(boolean isShowCompleted);

}
