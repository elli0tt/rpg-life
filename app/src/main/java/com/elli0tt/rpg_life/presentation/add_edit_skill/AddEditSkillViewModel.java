package com.elli0tt.rpg_life.presentation.add_edit_skill;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Skill;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.skills.InsertSkillsUseCase;

public class AddEditSkillViewModel extends AndroidViewModel {

    private MutableLiveData<String> name = new MutableLiveData<>();

    private InsertSkillsUseCase insertSkillsUseCase;

    public AddEditSkillViewModel(@NonNull Application application) {
        super(application);

        SkillsRepository skillsRepository = new SkillsRepositoryImpl(application);

        insertSkillsUseCase = new InsertSkillsUseCase(skillsRepository);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    void saveSkill(){
        Skill skillToSave = new Skill(name.getValue());

        insertSkillsUseCase.invoke(skillToSave);
    }

}
