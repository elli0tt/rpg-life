package com.elli0tt.rpg_life.presentation.screen.add_edit_quest;

import android.app.Application;
import android.content.ContentValues;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.UserRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.UserRepository;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetClosestWeekdayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetNextWeekCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.CompleteQuestUseCase;
import com.elli0tt.rpg_life.presentation.worker.InsertEmptySubQuestWorker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddEditQuestViewModel extends AndroidViewModel {
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>("");
    private MutableLiveData<Difficulty> difficulty =
            new MutableLiveData<>(Difficulty.NOT_SET);

    private MutableLiveData<Quest.DateState> dateDueState =
            new MutableLiveData<>(Quest.DateState.NOT_SET);
    private MutableLiveData<Quest.DateState> startDateState =
            new MutableLiveData<>(Quest.DateState.NOT_SET);

    private MutableLiveData<Quest.ReminderState> reminderState =
            new MutableLiveData<>(Quest.ReminderState.NOT_SET);

    private MutableLiveData<Integer> repeatTextResId =
            new MutableLiveData<>(R.string.add_edit_quest_repeat);

    private MutableLiveData<Quest.RepeatState> repeatState =
            new MutableLiveData<>(Quest.RepeatState.NOT_SET);

    private Calendar startDate = Calendar.getInstance();
    private Calendar dateDue = Calendar.getInstance();
    private Calendar reminderDate = Calendar.getInstance();

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

    private final String today;
    private final String tomorrow;

    private CompleteQuestUseCase completeQuestUseCase;

    private QuestsRepository questsRepository;
    private SkillsRepository skillsRepository;
    private UserRepository userRepository;

    private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

    private WorkManager workManager;
    private OneTimeWorkRequest insertEmptyQuestWorkRequest;

    private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("d MMM, yyyy HH:mm",
            Locale.getDefault());

    public AddEditQuestViewModel(@NonNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        updateInsertEmptyQuestWorkRequest();

        questsRepository = new QuestsRepositoryImpl(application);
        skillsRepository = new SkillsRepositoryImpl(application);
        userRepository = new UserRepositoryImpl(application);

        completeQuestUseCase = new CompleteQuestUseCase(questsRepository, skillsRepository,
                userRepository);

        today = application.getString(R.string.quest_date_due_today);
        tomorrow = application.getString(R.string.quest_date_due_tomorrow);
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

    LiveData<Quest.DateState> getDateDueState() {
        return dateDueState;
    }

    LiveData<Quest.DateState> getStartDateState() {
        return startDateState;
    }

    LiveData<Quest.ReminderState> getReminderState() {
        return reminderState;
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

    long getReminderTime() {
        return reminderDate.getTimeInMillis();
    }

    LiveData<WorkInfo> getInsertEmptyQuestWorkInfo() {
        return workManager.getWorkInfoByIdLiveData(insertEmptyQuestWorkRequest.getId());
    }

    void start(@Nullable Integer id, boolean isSubQuest, int parentQuestId) {
        this.isSubQuest = isSubQuest;
        this.parentQuestId = parentQuestId;

        if (id == null) {
            //No need to populate, the quest is new
            currentQuest = new Quest();
            currentQuest.setName("");
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

    private void onDataLoaded(@NonNull Quest quest) {
        name.postValue(quest.getName());
        description.postValue(quest.getDescription());
        difficulty.postValue(quest.getDifficulty());
        dateDueState.postValue(quest.getDateDueState());
        startDateState.postValue(quest.getStartDateState());
        dateDue = quest.getDateDue();
        startDate = quest.getStartDate();
        repeatState.postValue(quest.getRepeatState());
        repeatTextResId.postValue(getRepeatTextResId(quest.getRepeatState()));
        isDataLoaded = true;
    }

    int getRepeatTextResId(Quest.RepeatState repeatState) {
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

    void saveQuest() {
        Quest quest = new Quest();
        quest.setName(name.getValue() == null ? "" : name.getValue());
        quest.setDescription(description.getValue() == null ? "" : description.getValue());
        quest.setDifficulty(difficulty.getValue() == null ? Difficulty.NOT_SET :
                difficulty.getValue());
        quest.setDateDue(dateDue);
        quest.setStartDate(startDate);
        quest.setDateDueState(dateDueState.getValue() == null ? Quest.DEFAULT_DATE_DUE_STATE :
                dateDueState.getValue());
        quest.setStartDateState(startDateState.getValue() == null ?
                Quest.DEFAULT_START_DATE_STATE : startDateState.getValue());
        quest.setRepeatState(repeatState.getValue() == null ? Quest.DEFAULT_REPEAT_STATE :
                repeatState.getValue());
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
    }

    String getDateDueFormatted() {
        if (new IsCalendarEqualsTodayCalendarUseCase().invoke(dateDue)) {
            return today;
        }
        if (new IsCalendarEqualsTomorrowCalendarUseCase().invoke(dateDue)) {
            return tomorrow;
        }
        return dateFormat.format(dateDue.getTime());
    }

    String getTimeDueFormatted() {
        return timeFormat.format(dateDue.getTime());
    }

    String getStartDateFormatted() {
        if (new IsCalendarEqualsTodayCalendarUseCase().invoke(startDate)) {
            return today;
        }
        if (new IsCalendarEqualsTomorrowCalendarUseCase().invoke(startDate)) {
            return tomorrow;
        }

        return dateFormat.format(startDate.getTime());
    }

    String getStartTimeFormatted() {
        return timeFormat.format(startDate.getTime());
    }

    String getReminderDateFormatted() {
        if (new IsCalendarEqualsTodayCalendarUseCase().invoke(reminderDate)) {
            return today + " " + timeFormat.format(reminderDate.getTime());
        }
        if (new IsCalendarEqualsTomorrowCalendarUseCase().invoke(reminderDate)) {
            return tomorrow + " " + timeFormat.format(reminderDate.getTime());
        }

        return dateFormat.format(reminderDate.getTime()) + " " + timeFormat.format(reminderDate.getTime());
    }

    void setDateDue(int year, int month, int dayOfMonth) {
        dateDue.set(Calendar.YEAR, year);
        dateDue.set(Calendar.MONTH, month);
        dateDue.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setDateDue(int hourOfDay, int minutes) {
        dateDue.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateDue.set(Calendar.MINUTE, minutes);
        dateDueState.setValue(Quest.DateState.DATE_TIME_SET);
    }

    void removeDateDue() {
        dateDueState.setValue(Quest.DateState.NOT_SET);
        removeRepeat();
    }

    void removeTimeDue() {
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setDateDueToday() {
        dateDue = new GetTodayCalendarUseCase().invoke();
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setDateDueTomorrow() {
        dateDue = new GetTomorrowCalendarUseCase().invoke();
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setDateDueNextWeek() {
        dateDue = new GetNextWeekCalendarUseCase().invoke();
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setDateDueClosestWeekday() {
        dateDue = new GetClosestWeekdayCalendarUseCase().invoke();
        dateDueState.setValue(Quest.DateState.DATE_SET);
    }

    void setStartDateToday() {
        startDate = new GetTodayCalendarUseCase().invoke();
        startDateState.setValue(Quest.DateState.DATE_SET);
    }

    void setStartDateTomorrow() {
        startDate = new GetTomorrowCalendarUseCase().invoke();
        startDateState.setValue(Quest.DateState.DATE_SET);
    }

    void setStartDateNextWeek() {
        startDate = new GetNextWeekCalendarUseCase().invoke();
        startDateState.setValue(Quest.DateState.DATE_SET);
    }

    void setStartDate(int year, int month, int dayOfMonth) {
        startDate.set(year, month, dayOfMonth);
        startDateState.setValue(Quest.DateState.DATE_SET);
    }

    void setStartTime(int hourOfDay, int minutes) {
        startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        startDate.set(Calendar.MINUTE, minutes);
        startDateState.setValue(Quest.DateState.DATE_TIME_SET);
    }

    void removeStartDate() {
        startDateState.setValue(Quest.DateState.NOT_SET);
    }

    void removeStartTime() {
        startDateState.setValue(Quest.DateState.DATE_SET);
    }

    void setReminderDate(int year, int month, int dayOfMonth) {
        reminderDate.set(year, month, dayOfMonth);
    }

    void setReminderTime(int hourOfDay, int minutes) {
        reminderDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reminderDate.set(Calendar.MINUTE, minutes);
        reminderDate.set(Calendar.SECOND, 0);
        reminderState.setValue(Quest.ReminderState.PICK_CUSTOM_DATE);
    }

    void setRepeatState(Quest.RepeatState repeatState) {
        this.repeatState.setValue(repeatState);
        repeatTextResId.setValue(getRepeatTextResId(repeatState));
        if (dateDueState.getValue() != null) {
            if (repeatState.equals(Quest.RepeatState.WEEKDAYS)) {
                setDateDueClosestWeekday();
            } else if (!repeatState.equals(Quest.RepeatState.NOT_SET)) {
                setDateDueToday();
            }
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

    ContentValues getQuestContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, name.getValue());
        contentValues.put(CalendarContract.Events.DTSTART, startDate.getTimeInMillis());
        contentValues.put(CalendarContract.Events.DTEND, dateDue.getTimeInMillis());
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE,
                Calendar.getInstance().getTimeZone().getID());
        return contentValues;
    }

    void updateInsertEmptyQuestWorkRequest() {
        Data inputData = new Data.Builder()
                .putInt(com.elli0tt.rpg_life.presentation.worker.Constants.KEY_PARENT_QUEST_ID,
                        parentQuestId)
                .build();
        insertEmptyQuestWorkRequest =
                new OneTimeWorkRequest.Builder(InsertEmptySubQuestWorker.class)
                        .setInputData(inputData)
                        .build();
    }

    void insertEmptyQuest() {
        workManager.enqueue(insertEmptyQuestWorkRequest);
    }

    int getSubQuestId(int position) {
        if (subQuests.getValue() == null) {
            return Quest.DEFAULT_ID;
        }
        return subQuests.getValue().get(position).getId();
    }

    int getDateDueColor(Calendar dateDue) {
        if (Calendar.getInstance().after(dateDue)) {
            return R.color.colorAfterDateDue;
        }
        return R.color.colorBeforeDateDue;
    }

    String getDateDueFormatted(Quest.DateState dateDueState, Calendar dateDue) {
        if (new IsCalendarEqualsTodayCalendarUseCase().invoke(dateDue)) {
            return today;
        } else if (new IsCalendarEqualsTomorrowCalendarUseCase().invoke(dateDue)) {
            return tomorrow;
        } else if (dateDueState.equals(Quest.DateState.DATE_SET)) {
            return dateFormat.format(dateDue.getTime());
        } else {
            return dateAndTimeFormat.format(dateDue.getTime());
        }
    }
}