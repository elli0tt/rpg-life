package com.elli0tt.rpg_life.domain.use_case.quests;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import javax.inject.Inject;

public class SetQuestImportantUseCase {
    private final QuestsRepository questsRepository;

    @Inject
    public SetQuestImportantUseCase(QuestsRepository questsRepository) {
        this.questsRepository = questsRepository;
    }

    public void invoke(Quest quest, boolean isImportant) {
        quest.setImportant(isImportant);
        questsRepository.updateQuests(quest);
    }
}
