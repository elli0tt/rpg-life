package com.elli0tt.rpg_life.domain.use_case.quests;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class SetQuestImportantUseCase {
    private QuestsRepository questsRepository;

    public SetQuestImportantUseCase(QuestsRepository questsRepository) {
        this.questsRepository = questsRepository;
    }

    public void invoke(Quest quest, boolean isImportant) {
        quest.setImportant(isImportant);
        questsRepository.updateQuests(quest);
    }
}
