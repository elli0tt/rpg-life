package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class SetQuestCompletedUseCase {
    private UpdateQuestUseCase updateQuestUseCase;

    public SetQuestCompletedUseCase(QuestsRepository repository){
        updateQuestUseCase = new UpdateQuestUseCase(repository);
    }

    public void invoke(Quest quest, boolean isCompleted){
        quest.setCompleted(isCompleted);
        updateQuestUseCase.invoke(quest);
    }
}
