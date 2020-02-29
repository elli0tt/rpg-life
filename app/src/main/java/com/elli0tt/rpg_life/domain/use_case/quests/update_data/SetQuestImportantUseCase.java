package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class SetQuestImportantUseCase {
    private UpdateQuestsUseCase updateQuestsUseCase;

    public SetQuestImportantUseCase(QuestsRepository repository){
        updateQuestsUseCase = new UpdateQuestsUseCase(repository);
    }

    public void invoke(Quest quest, boolean isImportant){
        quest.setImportant(isImportant);
        updateQuestsUseCase.invoke(quest);
    }
}
