package com.elli0tt.rpg_life.presentation.screen.quests;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.model.QuestsFilterState;
import com.elli0tt.rpg_life.domain.model.QuestsSortingState;
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

import javax.inject.Inject;

public class QuestsViewModel extends ViewModel {

    private final LiveData<List<Quest>> allQuests;
    private final MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();
    private final MediatorLiveData<List<Quest>> questsToShow = new MediatorLiveData<>();
    private final MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSelectionStarted = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isShowCompleted = new MutableLiveData<>();
    private final LiveData<Integer> showCompletedTextResId;
    private final SortQuestsUseCase sortQuestsUseCase;
    private final FilterQuestsUseCase filterQuestsUseCase;
    private final PopulateWithSamplesUseCase populateWithSamplesUseCase;
    private final CompleteQuestUseCase completeQuestUseCase;
    private final SetQuestImportantUseCase setQuestImportantUseCase;
    private final QuestsRepository questsRepository;
    private final SkillsRepository skillsRepository;
    private final UserRepository userRepository;
    private final WorkManager workManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy",
            Locale.getDefault());
    private final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("d MMM, yyyy HH:mm",
            Locale.getDefault());
    private String today;
    private String tomorrow;
    private OneTimeWorkRequest insertEmptyQuestWorkRequest;
    private OneTimeWorkRequest insertEmptyChallengeWorkRequest;

    @Inject
    public QuestsViewModel(
            final SortQuestsUseCase sortQuestsUseCase,
            final FilterQuestsUseCase filterQuestsUseCase,
            final PopulateWithSamplesUseCase populateWithSamplesUseCase,
            final CompleteQuestUseCase completeQuestUseCase,
            final SetQuestImportantUseCase setQuestImportantUseCase,
            final QuestsRepository questsRepository,
            final SkillsRepository skillsRepository,
            final UserRepository userRepository,
            final WorkManager workManager
    ) {
        this.sortQuestsUseCase = sortQuestsUseCase;
        this.filterQuestsUseCase = filterQuestsUseCase;
        this.populateWithSamplesUseCase = populateWithSamplesUseCase;
        this.completeQuestUseCase = completeQuestUseCase;
        this.setQuestImportantUseCase = setQuestImportantUseCase;
        this.questsRepository = questsRepository;
        this.skillsRepository = skillsRepository;
        this.userRepository = userRepository;
        this.workManager = workManager;

        updateInsertEmptyQuestWorkRequest();
        updateInsertEmptyChallengeWorkRequest();

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

    public void start(String today, String tomorrow) {
        this.today = today;
        this.tomorrow = tomorrow;
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

    public int getDateDueColor(Calendar dateDue) {
        if (Calendar.getInstance().after(dateDue)) {
            return R.color.colorAfterDateDue;
        }
        return R.color.colorLightGreen;
    }

    public String getDateDueFormatted(Quest.DateState dateDueState, Calendar dateDue) {
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
