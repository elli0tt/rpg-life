package com.elli0tt.rpg_life.domain.use_case.quests.update_data;

import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase;

import java.util.ArrayList;
import java.util.List;

public class PopulateWithSamplesUseCase {
    private InsertQuestsUseCase insertQuestsUseCase;

    public PopulateWithSamplesUseCase(QuestsRepository repository) {
        insertQuestsUseCase = new InsertQuestsUseCase(repository);
    }

    private Quest[] generateSampleQuestsList() {
        Quest[] result = new Quest[10];
        for (int i = 0; i < 10; ++i) {
            result[i] = new Quest("Quest " + i);
        }
        return result;
    }

    public void invoke(){
        insertQuestsUseCase.invoke(generateSampleQuestsList());
    }

}
