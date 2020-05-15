package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.repository.SkillsRepository;
import com.elli0tt.rpg_life.domain.use_case.quests.FilterQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.SortQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.CompleteQuestUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.PopulateWithSamplesUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.SetQuestImportantUseCase;

import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private LiveData<List<Quest>> allQuests;

    private MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();
    private MediatorLiveData questsToShow = new MediatorLiveData<>();
    private MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSelectionStarted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isShowCompleted = new MutableLiveData<>();
    private LiveData<Integer> showCompletedTextResId;

    private SortQuestsUseCase sortQuestsUseCase = new SortQuestsUseCase();
    private FilterQuestsUseCase filterQuestsUseCase = new FilterQuestsUseCase();

    private PopulateWithSamplesUseCase populateWithSamplesUseCase;
    private CompleteQuestUseCase completeQuestUseCase;
    private SetQuestImportantUseCase setQuestImportantUseCase;

    private QuestsRepository questsRepository;
    private SkillsRepository skillsRepository;

    public QuestsViewModel(@NonNull Application application) {
        super(application);

        questsRepository = new QuestsRepositoryImpl(application);
        skillsRepository = new SkillsRepositoryImpl(application);

        populateWithSamplesUseCase = new PopulateWithSamplesUseCase(questsRepository);
        completeQuestUseCase = new CompleteQuestUseCase(questsRepository, skillsRepository);
        setQuestImportantUseCase = new SetQuestImportantUseCase(questsRepository);

        currentFilterState.setValue(questsRepository.getQuestsFilterState());
        currentSortingState.setValue(questsRepository.getQuestsSortingState());
        isShowCompleted.setValue(questsRepository.isShowCompleted());

        allQuests = questsRepository.getAllQuests();

        questsToShow.addSource(allQuests, (Observer<List<Quest>>) quests -> {
            if (quests != null
                    && currentSortingState.getValue() != null
                    && currentFilterState.getValue() != null
                    && isShowCompleted.getValue() != null) {
                questsToShow.setValue(sortQuestsUseCase.invoke(filterQuestsUseCase.invoke(quests,
                        currentFilterState.getValue(), isShowCompleted.getValue()),
                        currentSortingState.getValue()));
            }
        });

        questsToShow.addSource(currentFilterState, (Observer<QuestsFilterState>) filterState -> {
            if (allQuests.getValue() != null
                    && currentSortingState.getValue() != null
                    && isShowCompleted.getValue() != null) {
                questsToShow.setValue(sortQuestsUseCase.invoke(filterQuestsUseCase.invoke(allQuests.getValue(),
                        filterState, isShowCompleted.getValue()),
                        currentSortingState.getValue()));
            }
        });

        questsToShow.addSource(currentSortingState,
                (Observer<QuestsSortingState>) questsSortingState -> {
                    if (allQuests.getValue() != null) {
                        questsToShow.setValue(sortQuestsUseCase.invoke((List<Quest>) questsToShow.getValue(),
                                questsSortingState));
                    }
                });

        questsToShow.addSource(isShowCompleted, (Observer<Boolean>) isShowCompleted -> {
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

    public void insert(List<Quest> questList) {
        questsRepository.insert((Quest[]) questList.toArray());
    }

    void deleteAll() {
        questsRepository.deleteAll();
    }

    void populateWithSamples() {
        populateWithSamplesUseCase.invoke();
    }

    public void delete(List<Quest> questList) {
        questsRepository.delete(questList.toArray(new Quest[0]));
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
}
