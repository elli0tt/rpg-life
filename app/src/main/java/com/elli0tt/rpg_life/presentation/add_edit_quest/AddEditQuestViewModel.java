package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetCurrentDayOfMonthUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetCurrentHourOfDayUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetCurrentMinuteUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetCurrentMonthUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetCurrentYearUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetNextWeekCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetQuestDateDueStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetRepeatTextResIdUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.GetQuestByIdUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.GetQuestsByIdUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.load_data.GetSubQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.CompleteQuestUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.UpdateQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.skills.GetSkillsNamesByIdsUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditQuestViewModel extends AndroidViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>("");
    private Quest.Difficulty difficulty = Quest.Difficulty.NOT_SET;

    private MutableLiveData<Integer> nameErrorMessageId = new MutableLiveData<>();

    private MutableLiveData<Boolean> isDateDueSet = new MutableLiveData<>(false);

    private MutableLiveData<Integer> repeatTextResId =
            new MutableLiveData<>(R.string.add_edit_quest_repeat);

    private MutableLiveData<Quest.RepeatState> repeatState =
            new MutableLiveData<>(Quest.RepeatState.NOT_SET);

    private MutableLiveData<List<Integer>> relatedSkillsIds = new MutableLiveData<>();

    private LiveData<List<String>> skillsNames;

    private Calendar dateDue = Calendar.getInstance();

    private Quest currentQuest;
    private Quest parentQuest;

    private LiveData<List<Quest>> subQuests;

    private boolean isSubQuest;
    private int parentQuestId;

    /**
     * Id of quest to open in edit mode
     */
    private int id;

    private boolean isNewQuest = false;
    private boolean isDataLoaded = false;

    private final String TODAY;
    private final String TOMORROW;

    private GetQuestDateDueStateUseCase getQuestDateDueStateUseCase =
            new GetQuestDateDueStateUseCase();
    private GetCurrentYearUseCase getCurrentYearUseCase = new GetCurrentYearUseCase();
    private GetCurrentMonthUseCase getCurrentMonthUseCase = new GetCurrentMonthUseCase();
    private GetCurrentDayOfMonthUseCase getCurrentDayOfMonthUseCase =
            new GetCurrentDayOfMonthUseCase();
    private GetCurrentHourOfDayUseCase getCurrentHourOfDayUseCase =
            new GetCurrentHourOfDayUseCase();
    private GetCurrentMinuteUseCase getCurrentMinuteUseCase = new GetCurrentMinuteUseCase();
    private GetRepeatTextResIdUseCase getRepeatTextResIdUseCase = new GetRepeatTextResIdUseCase();

    private InsertQuestsUseCase insertQuestsUseCase;
    private UpdateQuestsUseCase updateQuestsUseCase;
    private GetQuestByIdUseCase getQuestByIdUseCase;
    private GetQuestsByIdUseCase getQuestsByIdUseCase;
    private GetSubQuestsUseCase getSubQuestsUseCase;
    private CompleteQuestUseCase completeQuestUseCase;
    private DeleteQuestsUseCase deleteQuestsUseCase;
    private GetSkillsNamesByIdsUseCase getSkillsNamesByIdsUseCase;

    public AddEditQuestViewModel(@NonNull Application application) {
        super(application);

        QuestsRepository questsRepository = new QuestsRepositoryImpl(application);
        SkillsRepository skillsRepository = new SkillsRepositoryImpl(application);

        insertQuestsUseCase = new InsertQuestsUseCase(questsRepository);
        updateQuestsUseCase = new UpdateQuestsUseCase(questsRepository);
        getQuestByIdUseCase = new GetQuestByIdUseCase(questsRepository);
        getQuestsByIdUseCase = new GetQuestsByIdUseCase(questsRepository);
        getSubQuestsUseCase = new GetSubQuestsUseCase(questsRepository);
        completeQuestUseCase = new CompleteQuestUseCase(questsRepository, skillsRepository);
        deleteQuestsUseCase = new DeleteQuestsUseCase(questsRepository);

        getSkillsNamesByIdsUseCase = new GetSkillsNamesByIdsUseCase(skillsRepository);

        TODAY = application.getString(R.string.quest_date_due_today);
        TOMORROW = application.getString(R.string.quest_date_due_tomorrow);

        skillsNames = Transformations.map(relatedSkillsIds, new Function<List<Integer>,
                List<String>>() {
            @Override
            public List<String> apply(List<Integer> ids) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getSkillsNamesByIdsUseCase.invoke(ids);
                    }
                }.start();
                return null;
            }
        });
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getDescription() {
        return description;
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
        return isNewQuest;
    }

    int getQuestId() {
        return currentQuest.getId();
    }

    void start(@Nullable Integer id, boolean isSubQuest, int parentQuestId) {
        this.isSubQuest = isSubQuest;
        this.parentQuestId = parentQuestId;

        if (id == null) {
            //No need to populate, the quest is new
            currentQuest = new Quest();
            isNewQuest = true;
            subQuests = new MutableLiveData<>();
            return;
        }

        this.id = id;
        subQuests = getSubQuestsUseCase.invoke(id);

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
                currentQuest = getQuestByIdUseCase.invoke(id);
                onDataLoaded(currentQuest);
            }
        }.start();
    }

    private void loadParentQuest() {
        new Thread() {
            @Override
            public void run() {
                parentQuest = getQuestByIdUseCase.invoke(parentQuestId);
            }
        }.start();
    }

    private void onDataLoaded(Quest quest) {
        name.postValue(quest.getName());
        description.postValue(quest.getDescription());
        difficulty = quest.getDifficulty();
        isDateDueSet.postValue(quest.isDateDueSet());
        dateDue = quest.getDateDue();
        repeatState.postValue(quest.getRepeatState());
        repeatTextResId.postValue(getRepeatTextResIdUseCase.invoke(quest.getRepeatState()));
        relatedSkillsIds.postValue(quest.getRelatedSkillsIds());
        isDataLoaded = true;
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

        Quest quest = new Quest();
        quest.setName(name.getValue());
        quest.setDescription(description.getValue());
        quest.setDifficulty(difficulty);
        quest.setDateDue(dateDue);
        quest.setIsDateDueSet(isDateDueSet.getValue());
        quest.setRepeatState(repeatState.getValue());
        quest.setIsSubQuest(isSubQuest);
        quest.setParentQuestId(parentQuestId);


        if (isNewQuest) {
            insertQuestsUseCase.invoke(quest);
        } else {
            quest.setId(id);
            quest.setCompleted(currentQuest.isCompleted());
            quest.setImportant(currentQuest.isImportant());
            updateQuestsUseCase.invoke(quest);
        }
        return true;
    }

    int getCurrentYear() {
        return getCurrentYearUseCase.invoke();
    }

    int getCurrentMonth() {
        return getCurrentMonthUseCase.invoke();
    }

    int getCurrentDayOfMonth() {
        return getCurrentDayOfMonthUseCase.invoke();
    }

    int getCurrentHourOfDay() {
        return getCurrentHourOfDayUseCase.invoke();
    }

    int getCurrentMinute() {
        return getCurrentMinuteUseCase.invoke();
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
        repeatTextResId.setValue(getRepeatTextResIdUseCase.invoke(repeatState));
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
            deleteQuestsUseCase.invoke(listToDelete.toArray(new Quest[0]));
        }
    }

    void changeDifficulty(int popUpMenuItemId) {
        switch (popUpMenuItemId) {
            case Constants.VERY_EASY_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.VERY_EASY;
                break;
            case Constants.EASY_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.EASY;
                break;
            case Constants.NORMAL_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.NORMAL;
                break;
            case Constants.HARD_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.HARD;
                break;
            case Constants.VERY_HARD_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.VERY_HARD;
                break;
            case Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ID:
                difficulty = Quest.Difficulty.IMPOSSIBLE;
                break;
        }
    }
}
