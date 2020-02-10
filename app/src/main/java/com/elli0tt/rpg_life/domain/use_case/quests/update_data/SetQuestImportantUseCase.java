package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class SetQuestImportantUseCase {
    private UpdateQuestUseCase updateQuestUseCase;

    public SetQuestImportantUseCase(QuestsRepository repository){
        updateQuestUseCase = new UpdateQuestUseCase(repository);
    }

    public void invoke(Quest quest, boolean isImportant){
        quest.setImportant(isImportant);
        updateQuestUseCase.invoke(quest);
    }
}
