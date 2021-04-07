package com.elli0tt.rpg_life.presentation.screen.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.UserRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.repository.UserRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTodayCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.IsCalendarEqualsTomorrowCalendarUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.CompleteQuestUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.FilterQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.PopulateWithSamplesUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.SetQuestImportantUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.SortQuestsUseCase;
import com.elli0tt.rpg_life.presentation.worker.InsertEmptyChallengeWorker;
import com.elli0tt.rpg_life.presentation.worker.InsertEmptyQuestWorker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuestsViewModel extends AndroidViewModel {
    private final String today;
    private final String tomorrow;
    private LiveData<List<Quest>> allQuests;
    private MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();
    private MediatorLiveData<List<Quest>> questsToShow = new MediatorLiveData<>();
    private MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSelectionStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isShowCompleted = new MutableLiveData<>();
    private LiveData<Integer> showCompletedTextResId;
    private SortQuestsUseCase sortQuestsUseCase = new SortQuestsUseCase();
    private FilterQuestsUseCase filterQuestsUseCase =
            new FilterQuestsUseCase(new IsCalendarEqualsTodayCalendarUseCase(),
                    new IsCalendarEqualsTomorrowCalendarUseCase());
    private PopulateWithSamplesUseCase populateWithSamplesUseCase;
    private CompleteQuestUseCase completeQuestUseCase;
    private SetQuestImportantUseCase setQuestImportantUseCase;
    private QuestsRepository questsRepository;
    private SkillsRepository skillsRepository;
    private UserRepository userRepository;
    private WorkManager workManager;
    private OneTimeWorkRequest insertEmptyQuestWorkRequest;
    private OneTimeWorkRequest insertEmptyChallengeWorkRequest;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
    private SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("d MMM, yyyy HH:mm",
            Locale.getDefault());

    public QuestsViewModel(@NonNull Application application) {
        super(application);
        workManager = WorkManager.getInstance(application);
        updateInsertEmptyQuestWorkRequest();
        updateInsertEmptyChallengeWorkRequest();

        today = application.getString(R.string.quest_date_due_today);
        tomorrow = application.getString(R.string.quest_date_due_tomorrow);

        questsRepository = new QuestsRepositoryImpl(application);
        skillsRepository = new SkillsRepositoryImpl(application);
        userRepository = new UserRepositoryImpl(application);

        populateWithSamplesUseCase = new PopulateWithSamplesUseCase(questsRepository);
        completeQuestUseCase = new CompleteQuestUseCase(questsRepository, skillsRepository,
                userRepository);
        setQuestImportantUseCase = new SetQuestImportantUseCase(questsRepository);

        currentFilterState.setValue(questsRepository.getQuestsFilterState());
        currentSortingState.setValue(questsRepository.getQuestsSortingState());
        isShowCompleted.setValue(questsRepository.isShowCompleted());

        allQuests = questsRepository.getAllQuests();

        questsToShow.addSource(allQuests, quests -> {
            if (quests != null
                    && currentSortingState.getValue() != null
                    && currentFilterState.getValue() != null
                    && isShowCompleted.getValue() != null) {

                questsToShow.setValue(sortQuestsUseCase.invoke(filterQuestsUseCase.invoke(quests,
                        currentFilterState.getValue(), isShowCompleted.getValue()),
                        currentSortingState.getValue()));
            }
        });

        questsToShow.addSource(currentFilterState, filterState -> {
            if (allQuests.getValue() != null
                    && currentSortingState.getValue() != null
                    && isShowCompleted.getValue() != null) {
                questsToShow.setValue(sortQuestsUseCase.invoke(filterQuestsUseCase.invoke(allQuests.getValue(),
                        filterState, isShowCompleted.getValue()),
                        currentSortingState.getValue()));
            }
        });

        questsToShow.addSource(currentSortingState,
                questsSortingState -> {
                    if (allQuests.getValue() != null) {
                        questsToShow.setValue(sortQuestsUseCase.invoke(questsToShow.getValue(),
                                questsSortingState));
                    }
                });

        questsToShow.addSource(isShowCompleted, isShowCompleted -> {
            if (allQuests.getValue() != null
                    && currentFilterState.getValue() != null
                    && currentSortingState.getValue() != null) {
                questsToShow.setValue(sortQuestsUseCase.invoke(filterQuestsUseCase.invoke(allQuests.getValue(),
                        currentFilterState.getValue(), isShowCompleted),
                        currentSortingState.getValue()));
            }
        });

        showCompletedTextResId = Transformations.map(isShowCompleted, isShowCompleted -> {
            if (isShowCompleted) {
                return R.string.hide_completed;
            }
            return R.string.show_completed;
        });

    }

    LiveData<List<Quest>> getQuests() {
        return questsToShow;
    }

    LiveData<Integer> getShowCompletedTextResId() {
        return showCompletedTextResId;
    }

    LiveData<Boolean> isSelectionStarted() {
        return isSelectionStarted;
    }

    LiveData<WorkInfo> getInsertEmptyQuestWorkInfo() {
        return workManager.getWorkInfoByIdLiveData(insertEmptyQuestWorkRequest.getId());
    }

    LiveData<WorkInfo> getInsertEmptyChallengeWorkInfo() {
        return workManager.getWorkInfoByIdLiveData(insertEmptyChallengeWorkRequest.getId());
    }

    public void insert(List<Quest> questList) {
        questsRepository.insertQuests((Quest[]) questList.toArray());
    }

    void deleteAll() {
        questsRepository.deleteAllQuests();
    }

    void populateWithSamples() {
        populateWithSamplesUseCase.invoke();
    }

    public void delete(List<Quest> questList) {
        questsRepository.deleteQuests(questList.toArray(new Quest[0]));
    }

    void setFiltering(QuestsFilterState filterState) {
        currentFilterState.setValue(filterState);
        questsRepository.setQuestsFilterState(filterState);
    }

    void setSorting(QuestsSortingState sortingState) {
        currentSortingState.setValue(sortingState);
        questsRepository.setQuestsSoringState(sortingState);
    }

    void startSelection() {
        isSelectionStarted.setValue(true);
    }

    void finishSelection() {
        isSelectionStarted.setValue(false);
    }

    void completeQuest(int position, boolean isCompleted) {
        if (getQuests().getValue() != null) {
            Quest quest = getQuests().getValue().get(position);
            completeQuestUseCase.invoke(quest, isCompleted);
        }
    }

    void setQuestImportant(int position, boolean isImportant) {
        if (getQuests().getValue() != null) {
            setQuestImportantUseCase.invoke(getQuests().getValue().get(position), isImportant);
        }
    }

    void changeShowCompleted() {
        if (isShowCompleted.getValue() != null) {
            isShowCompleted.setValue(!isShowCompleted.getValue());
            questsRepository.setShowCompleted(isShowCompleted.getValue());
        }
    }

    void insertEmptyQuest() {
        workManager.enqueue(insertEmptyQuestWorkRequest);
    }

    void insertEmptyChallenge() {
        workManager.enqueue(insertEmptyChallengeWorkRequest);
    }

    void updateInsertEmptyQuestWorkRequest() {
        insertEmptyQuestWorkRequest =
                new OneTimeWorkRequest.Builder(InsertEmptyQuestWorker.class).build();
    }

    void updateInsertEmptyChallengeWorkRequest() {
        insertEmptyChallengeWorkRequest =
                new OneTimeWorkRequest.Builder(InsertEmptyChallengeWorker.class).build();
    }

    int getDateDueColor(Calendar dateDue) {
        if (Calendar.getInstance().after(dateDue)) {
            return R.color.colorAfterDateDue;
        }
        return R.color.colorLightGreen;
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
