package com.elli0tt.rpg_life.presentation.quests.add_edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

public class AddEditQuestViewModel extends AndroidViewModel {

    private QuestsRepositoryImpl repository;

    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<Integer> difficulty = new MutableLiveData<>();

    /**
     * Id of quest to open in edit mode
     */
    private int id;

    private boolean isNewQuest = false;
    private boolean isDataLoaded = false;

    public AddEditQuestViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public MutableLiveData<Integer> getDifficulty() {
        return difficulty;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public void setDifficulty(@Quest.Difficulty int difficulty) {
        this.difficulty.setValue(difficulty);
    }

    public void start(@Nullable Integer id) {
        if (id == null) {
            //No need to populate, the quest is new
            isNewQuest = true;
            return;
        }

        this.id = id;

        if (isDataLoaded) {
            //No need to populate, data have already been loaded
            return;
        }
        loadCurrentQuest();
    }

    private void loadCurrentQuest() {
        new Thread(){
            @Override
            public void run() {
                Quest currentQuest = repository.getQuestById(id);
                onDataLoaded(currentQuest);
            }
        }.start();
    }

    private void onDataLoaded(Quest quest) {
        name.postValue(quest.getName());
        description.postValue(quest.getDescription());
        difficulty.postValue(quest.getDifficulty());
        isDataLoaded = true;
    }


    public void saveQuest() {
        if (isNewQuest) {
            repository.insert(new Quest(
                    name.getValue(),
                    description.getValue(),
                    difficulty.getValue()));
        } else{
            updateQuest();
        }
    }

    public void updateQuest(){
        if (isNewQuest){
            throw new RuntimeException("updateQuest() was called for a new quest");
        }

        repository.update(new Quest(
                id,
                name.getValue(),
                description.getValue(),
                difficulty.getValue()));
    }
}
