package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetNextWeekCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.CompleteQuestUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditQuestViewModel extends AndroidViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>("");
    private MutableLiveData<Difficulty> difficulty =
            new MutableLiveData<>(Difficulty.NOT_SET);

    private MutableLiveData<Integer> nameErrorMessageId = new MutableLiveData<>();

    private MutableLiveData<Boolean> isDateDueSet = new MutableLiveData<>(false);

    private MutableLiveData<Integer> repeatTextResId =
            new MutableLiveData<>(R.string.add_edit_quest_repeat);

    private MutableLiveData<Quest.RepeatState> repeatState =
            new MutableLiveData<>(Quest.RepeatState.NOT_SET);

    private Calendar dateDue = Calendar.getInstance();

    private Quest currentQuest;

    private LiveData<List<Quest>> subQuests;

    private boolean isSubQuest;
    private int parentQuestId;

    /**
     * Id of quest to open in edit mode
     */
    private int id;

    private enum Mode {
        ADD, EDIT
    }

    private Mode mode = Mode.EDIT;

    private boolean isDataLoaded = false;

    private final String TODAY;
    private final String TOMORROW;

    private CompleteQuestUseCase completeQuestUseCase;

    private QuestsRepository questsRepository;
    private SkillsRepository skillsRepository;

    public AddEditQuestViewModel(@NonNull Application application) {
        super(application);
        questsRepository = new QuestsRepositoryImpl(application);
        skillsRepository = new SkillsRepositoryImpl(application);

        completeQuestUseCase = new CompleteQuestUseCase(questsRepository, skillsRepository);

        TODAY = application.getString(R.string.quest_date_due_today);
        TOMORROW = application.getString(R.string.quest_date_due_tomorrow);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    LiveData<Difficulty> getDifficulty() {
        return difficulty;
    }

    LiveData<Integer> getNameErrorMessageId() {
        return nameErrorMessageId;
    }

    LiveData<Boolean> isDateDueSet() {
        return isDateDueSet;
    }

    LiveData<Quest.RepeatState> getRepeatState() {
        return repeatState;
    }

    LiveData<Integer> getRepeatTextResId() {
        return repeatTextResId;
    }

    LiveData<List<Quest>> getSubQuests() {
        return subQuests;
    }

    boolean getIsNewQuest() {
        return mode.equals(Mode.ADD);
    }

    int getQuestId() {
        return currentQuest.getId();
    }

    void start(@Nullable Integer id, boolean isSubQuest, int parentQuestId) {
        this.isSubQuest = isSubQuest;
        this.parentQuestId = parentQuestId;

        if (id == null) {
            //No need to populate, the quest is new
            currentQuest = new Quest("");
            mode = Mode.ADD;
            subQuests = new MutableLiveData<>();
            return;
        }

        this.id = id;
        subQuests = questsRepository.getSubQuests(id);

        if (isDataLoaded) {
            //No need to populate, the quest is already loaded
            return;
        }
        loadCurrentQuest();
    }

    private void loadCurrentQuest() {
        new Thread() {
            @Override
            public void run() {
                currentQuest = questsRepository.getQuestById(id);
                onDataLoaded(currentQuest);
            }
        }.start();
    }

    private void onDataLoaded(Quest quest) {
        name.postValue(quest.getName());
        description.postValue(quest.getDescription());
        difficulty.postValue(quest.getDifficulty());
        isDateDueSet.postValue(quest.isDateDueSet());
        dateDue = quest.getDateDue();
        repeatState.postValue(quest.getRepeatState());
        repeatTextResId.postValue(getRepeatTextResId(quest.getRepeatState()));
        isDataLoaded = true;
    }

    public int getRepeatTextResId(Quest.RepeatState repeatState) {
        switch (repeatState) {
            case NOT_SET:
                return R.string.add_edit_quest_repeat;
            case DAILY:
                return R.string.add_edit_quest_repeat_popup_daily;
            case WEEKDAYS:
                return R.string.add_edit_quest_repeat_popup_weekdays;
            case WEEKENDS:
                return R.string.add_edit_quest_repeat_popup_weekends;
            case WEEKLY:
                return R.string.add_edit_quest_repeat_popup_weekly;
            case MONTHLY:
                return R.string.add_edit_quest_repeat_popup_monthly;
            case YEARLY:
                return R.string.add_edit_quest_repeat_popup_yearly;
            default:
                return 0;
        }
    }

    private boolean isNameValid() {
        return name.getValue() != null && !name.getValue().isEmpty();
    }

    boolean saveQuest() {
        if (!isNameValid()) {
            nameErrorMessageId.setValue(R.string.add_edit_quest_name_error_message);
            return false;
        }

        nameErrorMessageId.setValue(null);

        Quest quest = new Quest(name.getValue());
        quest.setName(name.getValue());
        quest.setDescription(description.getValue());
        quest.setDifficulty(difficulty.getValue());
        quest.setDateDue(dateDue);
        quest.setDateDueSet(isDateDueSet.getValue());
        quest.setRepeatState(repeatState.getValue());
        quest.setSubQuest(isSubQuest);
        quest.setParentQuestId(parentQuestId);

        switch (mode) {
            case ADD:
                questsRepository.insertQuests(quest);
                break;
            case EDIT:
                quest.setId(id);
                quest.setCompleted(currentQuest.isCompleted());
                quest.setImportant(currentQuest.isImportant());
                questsRepository.updateQuests(quest);
                break;
        }
        if (isSubQuest) {
            questsRepository.updateQuestHasSubquestsById(parentQuestId, true);
        }
        return true;
    }

    String getDueDateFormatted() {
        if (new IsCalendarEqualsTodayCalendarUseCase().invoke(dateDue)) {
            return TODAY;
        }
        if (new IsCalendarEqualsTomorrowCalendarUseCase().invoke(dateDue)) {
            return TOMORROW;
        }
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
        isDateDueSet.setValue(true);
    }

    void removeDateDue() {
        isDateDueSet.setValue(false);
        removeRepeat();
    }

    void setDateDueToday() {
        dateDue = new GetTodayCalendarUseCase().invoke();
        isDateDueSet.setValue(true);
    }

    void setDateDueTomorrow() {
        dateDue = new GetTomorrowCalendarUseCase().invoke();
        isDateDueSet.setValue(true);
    }

    void setDateDueNextWeek() {
        dateDue = new GetNextWeekCalendarUseCase().invoke();
        isDateDueSet.setValue(true);
    }

    void setRepeatState(Quest.RepeatState repeatState) {
        this.repeatState.setValue(repeatState);
        repeatTextResId.setValue(getRepeatTextResId(repeatState));
        if (isDateDueSet.getValue() != null
                && isDateDueSet.getValue().equals(false)
                && !repeatState.equals(Quest.RepeatState.NOT_SET)) {
            setDateDueToday();
        }
    }

    void removeRepeat() {
        setRepeatState(Quest.RepeatState.NOT_SET);
    }

    void completeSubQuest(int position, boolean isCompleted) {
        if (subQuests.getValue() != null) {
            completeQuestUseCase.invoke(subQuests.getValue().get(position), isCompleted);
        }
    }

    void removeSubQuest(int position) {
        if (subQuests.getValue() != null) {
            List<Quest> listToDelete = new ArrayList<>();
            listToDelete.add(subQuests.getValue().get(position));
            questsRepository.deleteQuests(listToDelete.toArray(new Quest[0]));
        }
    }

    void changeDifficulty(int popUpMenuItemId) {
        switch (popUpMenuItemId) {
            case Constants.VERY_EASY_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.VERY_EASY);
                break;
            case Constants.EASY_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.EASY);
                break;
            case Constants.NORMAL_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.NORMAL);
                break;
            case Constants.HARD_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.HARD);
                break;
            case Constants.VERY_HARD_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.VERY_HARD);
                break;
            case Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ID:
                difficulty.setValue(Difficulty.IMPOSSIBLE);
                break;
        }
    }

    void removeDifficulty() {
        difficulty.setValue(Difficulty.NOT_SET);
    }
}
