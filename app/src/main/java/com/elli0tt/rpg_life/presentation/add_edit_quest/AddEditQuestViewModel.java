package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;

public class AddEditQuestViewModel extends AndroidViewModel {

    private QuestsRepositoryImpl repository;

    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<Integer> difficulty = new MutableLiveData<>();

    private MutableLiveData<String> nameErrorMessage = new MutableLiveData<>();

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

    public LiveData<String> getNameErrorMessage(){
        return nameErrorMessage;
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

    private boolean isNameValid(){
        return name.getValue() != null && !name.getValue().isEmpty();
    }


    public boolean saveQuest() {
        if (!isNameValid()){
            nameErrorMessage.setValue("Field can't be empty");
            return false;
        }

        //description field might be not filled by user, so it's necessary to set it manually to ""
        if (description.getValue() == null){
            description.setValue("");
        }

        nameErrorMessage.setValue(null);

        if (isNewQuest) {
            repository.insert(new Quest(
                    name.getValue(),
                    description.getValue(),
                    difficulty.getValue()));
        } else{
            repository.update(new Quest(
                    id,
                    name.getValue(),
                    description.getValue(),
                    difficulty.getValue()));
        }

        return true;

    }




}
