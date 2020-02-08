package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.use_case.DatePickerDialogUseCase;
import com.elli0tt.rpg_life.domain.use_case.GetQuestDateDueStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.TimePickerDialogUseCase;

import java.util.Calendar;

public class AddEditQuestViewModel extends AndroidViewModel {

    private QuestsRepositoryImpl repository;

    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<Integer> difficulty = new MutableLiveData<>();

    private MutableLiveData<String> nameErrorMessage = new MutableLiveData<>();

    private Calendar dateDue = Calendar.getInstance();

    private Quest currentQuest;

    private GetQuestDateDueStateUseCase getQuestDateDueStateUseCase;

    /**
     * Id of quest to open in edit mode
     */
    private int id;

    private boolean isNewQuest = false;
    private boolean isDataLoaded = false;

    private MutableLiveData<Quest.DateDueState> dateDueState =
            new MutableLiveData<>(Quest.DateDueState.NOT_SET);

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

    LiveData<String> getNameErrorMessage() {
        return nameErrorMessage;
    }

    LiveData<Quest.DateDueState> getDateDueState(){
        return dateDueState;
    }

    void start(@Nullable Integer id) {
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
        new Thread() {
            @Override
            public void run() {
                currentQuest = repository.getQuestById(id);
                onDataLoaded(currentQuest);
            }
        }.start();
    }

    private void onDataLoaded(Quest quest) {
        name.postValue(quest.getName());
        description.postValue(quest.getDescription());
        difficulty.postValue(quest.getDifficulty());
        dateDueState.postValue(quest.getDateDueState());
        dateDue = quest.getDateDue();
        isDataLoaded = true;
    }

    private boolean isNameValid() {
        return name.getValue() != null && !name.getValue().isEmpty();
    }


    boolean saveQuest() {
        if (!isNameValid()) {
            nameErrorMessage.setValue("Field can't be empty");
            return false;
        }

        //description field might be not filled by user, so it's necessary to set it manually to ""
        if (description.getValue() == null) {
            description.setValue("");
        }

        nameErrorMessage.setValue(null);

        if (isNewQuest) {
            repository.insert(new Quest(
                    name.getValue(),
                    description.getValue(),
                    difficulty.getValue(),
                    dateDue,
                    dateDueState.getValue()));
        } else {
            repository.update(new Quest(
                    id,
                    name.getValue(),
                    description.getValue(),
                    difficulty.getValue(),
                    dateDue,
                    currentQuest.isCompleted(),
                    currentQuest.isImportant(),
                    dateDueState.getValue()
            ));
        }
        return true;
    }

    int getCurrentYear() {
        return DatePickerDialogUseCase.getCurrentYear();
    }

    int getCurrentMonth() {
        return DatePickerDialogUseCase.getCurrentMonth();
    }

    int getCurrentDay() {
        return DatePickerDialogUseCase.getCurrentDay();
    }

    int getCurrentHour() {
        return TimePickerDialogUseCase.getCurrentHour();
    }

    int getCurrentMinute() {
        return TimePickerDialogUseCase.getCurrentMinute();
    }

    String getDueDateFormatted(){
        return Quest.getDateDueFormatted(dateDue);
    }

    void setDateDue(int year, int month, int dayOfMonth) {
        dateDue.set(Calendar.YEAR, year);
        dateDue.set(Calendar.MONTH, month);
        dateDue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    void setDateDue(int hourOfDay, int minutes) {
        dateDue.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateDue.set(Calendar.MINUTE, minutes);
        getQuestDateDueStateUseCase = new GetQuestDateDueStateUseCase();
        dateDueState.setValue(getQuestDateDueStateUseCase.invoke(dateDue));
    }

    void removeDateDue(){
        dateDueState.setValue(Quest.DateDueState.NOT_SET);
    }

}
