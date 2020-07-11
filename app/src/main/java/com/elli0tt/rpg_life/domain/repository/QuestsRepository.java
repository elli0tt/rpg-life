package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.RelatedToQuestSkills;
import com.elli0tt.rpg_life.presentation.screen.quests.QuestsFilterState;
import com.elli0tt.rpg_life.presentation.screen.quests.QuestsSortingState;

import java.util.List;

public interface QuestsRepository {
    Quest getQuestById(int id);

    List<Quest> getQuestsByIds(List<Integer> ids);

    LiveData<List<Quest>> getAllQuests();

    LiveData<List<Quest>> getSubQuests(int parentQuestId);

    LiveData<Quest> getQuestByIdLiveData(int questId);

    void insertQuests(Quest... quests);

    void updateQuests(Quest... quests);

    void updateQuestHasSubquestsById(int id, boolean hasSubquests);

    void deleteQuests(Quest... quests);

    void deleteAllQuests();

    QuestsFilterState getQuestsFilterState();

    void setQuestsFilterState(QuestsFilterState filterState);

    QuestsSortingState getQuestsSortingState();

    void setQuestsSoringState(QuestsSortingState sortingState);

    boolean isShowCompleted();

    void setShowCompleted(boolean isShowCompleted);

    LiveData<List<RelatedToQuestSkills>> getRelatedSkillsLiveData(int questId);

    List<RelatedToQuestSkills> getRelatedSkills(int questId);

    void insertRelatedSkill(int questId, int skillId, int xpPercentage);

    void deleteRelatedSkill(int questId, int skillId);

    int getCurrentId();

    void setCurrentId(int id);

    int getRelatedSkillId(int questId);

    void insertQuestWithRelatedSkills(Quest quest, int oldQuestId);

    long insertEmptyQuestSync();

    long insertEmptyChallengeSync();

    long insertEmptySubQuestSync(int parentQuestId);
}
