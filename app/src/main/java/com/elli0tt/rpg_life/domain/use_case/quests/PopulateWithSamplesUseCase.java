package com.elli0tt.rpg_life.domain.use_case.quests;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

public class PopulateWithSamplesUseCase {
    private QuestsRepository questsRepository;

    public PopulateWithSamplesUseCase(QuestsRepository repository) {
        questsRepository = repository;
    }

    private Quest[] generateSampleQuestsList() {
        Quest[] result = new Quest[10];
        for (int i = 0; i < 10; ++i) {
            result[i] = new Quest();
            result[i].setName("Quest " + i);
        }
        return result;
    }

    public void invoke() {
        questsRepository.insertQuests(generateSampleQuestsList());
    }

}
