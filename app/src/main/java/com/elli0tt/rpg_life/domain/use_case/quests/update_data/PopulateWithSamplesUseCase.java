package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;

import java.util.ArrayList;
import java.util.List;

public class PopulateWithSamplesUseCase {
    private InsertQuestsListUseCase insertQuestsListUseCase;

    public PopulateWithSamplesUseCase(QuestsRepository repository) {
        insertQuestsListUseCase = new InsertQuestsListUseCase(repository);
    }

    private List<Quest> generateSampleQuestsList() {
        List<Quest> result = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            result.add(new Quest("Quest " + i));
        }
        return result;
    }

    public void invoke(){
        insertQuestsListUseCase.invoke(generateSampleQuestsList());
    }

}
