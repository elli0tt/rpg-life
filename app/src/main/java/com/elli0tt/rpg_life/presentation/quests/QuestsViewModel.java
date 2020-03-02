package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetQuestDateDueStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.InsertQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.filter.FilterUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetAllQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetFilterStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetShowCompletedUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetSortingStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.sort.SortUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.CompleteQuestUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteAllQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.PopulateWithSamplesUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestImportantUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestsFilterStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestsSortingState;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetShowCompletedUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.UpdateQuestsDateDueStateUseCase;

import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private LiveData<List<Quest>> allQuests;

    private MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();

    private MediatorLiveData questsToShow = new MediatorLiveData<>();

    private MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSelectionStarted = new MutableLiveData<>(false);

    private MutableLiveData<Boolean> isShowCompleted = new MutableLiveData<>();

    private LiveData<Integer> showCompletedTextResId;

    private SortUseCase sortUseCase = new SortUseCase();
    private FilterUseCase filterUseCase = new FilterUseCase();

    private GetFilterStateUseCase getFilterStateUseCase;
    private GetSortingStateUseCase getSortingStateUseCase;
    private GetAllQuestsUseCase getAllQuestsUseCase;
    private GetShowCompletedUseCase getShowCompletedUseCase;

    private InsertQuestsUseCase insertQuestsUseCase;
    private DeleteQuestsUseCase deleteQuestsUseCase;
    private DeleteAllQuestsUseCase deleteAllQuestsUseCase;
    private SetQuestsFilterStateUseCase setQuestsFilterStateUseCase;
    private SetQuestsSortingState setQuestsSortingStateUseCase;
    private PopulateWithSamplesUseCase populateWithSamplesUseCase;
    private CompleteQuestUseCase completeQuestUseCase;
    private SetQuestImportantUseCase setQuestImportantUseCase;
    private UpdateQuestsDateDueStateUseCase updateQuestsDateDueStateUseCase;
    private SetShowCompletedUseCase setShowCompletedUseCase;

    private GetQuestDateDueStateUseCase getQuestDateDueStateUseCase =
            new GetQuestDateDueStateUseCase();

    public QuestsViewModel(@NonNull Application application) {
        super(application);

        QuestsRepository questsRepository = new QuestsRepositoryImpl(application);

        getFilterStateUseCase = new GetFilterStateUseCase(questsRepository);
        getSortingStateUseCase = new GetSortingStateUseCase(questsRepository);
        getAllQuestsUseCase = new GetAllQuestsUseCase(questsRepository);
        getShowCompletedUseCase = new GetShowCompletedUseCase(questsRepository);

        insertQuestsUseCase = new InsertQuestsUseCase(questsRepository);
        deleteQuestsUseCase = new DeleteQuestsUseCase(questsRepository);
        deleteAllQuestsUseCase = new DeleteAllQuestsUseCase(questsRepository);
        setQuestsFilterStateUseCase = new SetQuestsFilterStateUseCase(questsRepository);
        setQuestsSortingStateUseCase = new SetQuestsSortingState(questsRepository);
        populateWithSamplesUseCase = new PopulateWithSamplesUseCase(questsRepository);
        completeQuestUseCase = new CompleteQuestUseCase(questsRepository);
        setQuestImportantUseCase = new SetQuestImportantUseCase(questsRepository);
        updateQuestsDateDueStateUseCase = new UpdateQuestsDateDueStateUseCase(questsRepository);
        setShowCompletedUseCase = new SetShowCompletedUseCase(questsRepository);

        currentFilterState.setValue(getFilterStateUseCase.invoke());
        currentSortingState.setValue(getSortingStateUseCase.invoke());
        isShowCompleted.setValue(getShowCompletedUseCase.invoke());

        allQuests = getAllQuestsUseCase.invoke();


        questsToShow.addSource(allQuests, new Observer<List<Quest>>() {
            @Override
            public void onChanged(List<Quest> quests) {
                if (quests != null
                        && currentSortingState.getValue() != null
                        && currentFilterState.getValue() != null
                        && isShowCompleted.getValue() != null) {
                    questsToShow.setValue(sortUseCase.invoke(filterUseCase.invoke(quests,
                            currentFilterState.getValue(), isShowCompleted.getValue()),
                            currentSortingState.getValue()));
                }
            }
        });

        questsToShow.addSource(currentFilterState, new Observer<QuestsFilterState>() {
            @Override
            public void onChanged(QuestsFilterState filterState) {
                if (allQuests.getValue() != null
                        && currentSortingState.getValue() != null
                        && isShowCompleted.getValue() != null) {
                    questsToShow.setValue(sortUseCase.invoke(filterUseCase.invoke(allQuests.getValue(),
                            filterState, isShowCompleted.getValue()),
                            currentSortingState.getValue()));
                }
            }
        });

        questsToShow.addSource(currentSortingState, new Observer<QuestsSortingState>() {
            @Override
            public void onChanged(QuestsSortingState questsSortingState) {
                if (allQuests.getValue() != null) {
                    questsToShow.setValue(sortUseCase.invoke((List<Quest>) questsToShow.getValue(),
                            questsSortingState));
                }
            }
        });

        questsToShow.addSource(isShowCompleted, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isShowCompleted) {
                if (allQuests.getValue() != null
                        && currentFilterState.getValue() != null
                        && currentSortingState.getValue() != null) {
                    questsToShow.setValue(sortUseCase.invoke(filterUseCase.invoke(allQuests.getValue(),
                            currentFilterState.getValue(), isShowCompleted),
                            currentSortingState.getValue()));
                }
            }
        });

        showCompletedTextResId = Transformations.map(isShowCompleted, new Function<Boolean,
                Integer>() {
            @Override
            public Integer apply(Boolean isShowCompleted) {
                if (isShowCompleted) {
                    return R.string.hide_completed;
                }
                return R.string.show_completed;
            }
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
        insertQuestsUseCase.invoke((Quest[]) questList.toArray());
    }

    void deleteAll() {
        deleteAllQuestsUseCase.invoke();
    }

    void populateWithSamples() {
        populateWithSamplesUseCase.invoke();
    }

    public void delete(List<Quest> questList) {
        deleteQuestsUseCase.invoke(questList.toArray(new Quest[0]));
    }

    void setFiltering(QuestsFilterState filterState) {
        currentFilterState.setValue(filterState);
        setQuestsFilterStateUseCase.invoke(filterState);
    }

    void setSorting(QuestsSortingState sortingState) {
        currentSortingState.setValue(sortingState);
        setQuestsSortingStateUseCase.invoke(sortingState);
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

    void updateQuestsDateDueState() {
        if (getQuests().getValue() != null) {
            updateQuestsDateDueStateUseCase.invoke(getQuests().getValue());
        }
    }

    void changeShowCompleted() {
        if (isShowCompleted.getValue() != null) {
            isShowCompleted.setValue(!isShowCompleted.getValue());
            setShowCompletedUseCase.invoke(isShowCompleted.getValue());
        }
    }
}
