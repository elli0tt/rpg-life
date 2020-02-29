package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetQuestDateDueStateUseCase;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuestsDateDueStateUseCase {
    private GetQuestDateDueStateUseCase getQuestDateDueStateUseCase;
    private UpdateQuestsUseCase updateQuestsUseCase;

    public UpdateQuestsDateDueStateUseCase(QuestsRepository repository) {
        getQuestDateDueStateUseCase = new GetQuestDateDueStateUseCase();
        updateQuestsUseCase = new UpdateQuestsUseCase(repository);
    }

    public void invoke(List<Quest> quests) {
        List<Quest> questsToUpdate = new ArrayList<>();
        for (Quest quest : quests) {
            if (!getQuestDateDueStateUseCase.invoke(quest.getDateDue()).equals(quest.getDateDueState())) {
                questsToUpdate.add(quest);
            }
        }
        updateQuestsUseCase.invoke(questsToUpdate.toArray(new Quest[0]));
    }
}
