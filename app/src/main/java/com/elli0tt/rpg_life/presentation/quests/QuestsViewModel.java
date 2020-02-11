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

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.domain.repository.QuestsRepository;
import com.elli0tt.rpg_life.domain.use_case.add_edit_quest.GetQuestDateDueStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetActiveQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetAllQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetCompletedQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetFilterStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetImportantQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetSortingStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.sort.SortByDateAddedUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.sort.SortByDateDueUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.sort.SortByDiffucultyUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.sort.SortByNameUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteAllQuestsUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.DeleteQuestsListUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.InsertQuestsListUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.PopulateWithSamplesUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestCompletedUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestImportantUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestsFilterStateUseCase;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.SetQuestsSortingState;
import com.elli0tt.rpg_life.domain.use_case.quests.update_data.UpdateQuestsDateDueStateUseCase;

import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private LiveData<List<Quest>> quests;

    private MutableLiveData<QuestsFilterState> currentFilterState = new MutableLiveData<>();

    private MediatorLiveData questsToShow = new MediatorLiveData<>();

    private MutableLiveData<QuestsSortingState> currentSortingState = new MutableLiveData<>();

    private MutableLiveData<Boolean> isSelectionStarted = new MutableLiveData<>(false);

    private SortByNameUseCase sortByNameUseCase;
    private SortByDateAddedUseCase sortByDateAddedUseCase;
    private SortByDateDueUseCase sortByDateDueUseCase;
    private SortByDiffucultyUseCase sortByDiffucultyUseCase;

    private GetFilterStateUseCase getFilterStateUseCase;
    private GetSortingStateUseCase getSortingStateUseCase;
    private GetAllQuestsUseCase getAllQuestsUseCase;
    private GetActiveQuestsUseCase getActiveQuestsUseCase;
    private GetCompletedQuestsUseCase getCompletedQuestsUseCase;
    private GetImportantQuestsUseCase getImportantQuestsUseCase;

    private InsertQuestsListUseCase insertQuestsListUseCase;
    private DeleteQuestsListUseCase deleteQuestsListUseCase;
    private DeleteAllQuestsUseCase deleteAllQuestsUseCase;
    private SetQuestsFilterStateUseCase setQuestsFilterStateUseCase;
    private SetQuestsSortingState setQuestsSortingStateUseCase;
    private PopulateWithSamplesUseCase populateWithSamplesUseCase;
    private SetQuestCompletedUseCase setQuestCompletedUseCase;
    private SetQuestImportantUseCase setQuestImportantUseCase;
    private UpdateQuestsDateDueStateUseCase updateQuestsDateDueStateUseCase;

    private GetQuestDateDueStateUseCase getQuestDateDueStateUseCase;

    public QuestsViewModel(@NonNull Application application) {
        super(application);
        sortByNameUseCase = new SortByNameUseCase();
        sortByDateAddedUseCase = new SortByDateAddedUseCase();
        sortByDateDueUseCase = new SortByDateDueUseCase();
        sortByDiffucultyUseCase = new SortByDiffucultyUseCase();

        QuestsRepository questsRepository = new QuestsRepositoryImpl(application);

        getFilterStateUseCase = new GetFilterStateUseCase(questsRepository);
        getSortingStateUseCase = new GetSortingStateUseCase(questsRepository);
        getAllQuestsUseCase = new GetAllQuestsUseCase(questsRepository);
        getActiveQuestsUseCase = new GetActiveQuestsUseCase(questsRepository);
        getCompletedQuestsUseCase = new GetCompletedQuestsUseCase(questsRepository);
        getImportantQuestsUseCase = new GetImportantQuestsUseCase(questsRepository);

        insertQuestsListUseCase = new InsertQuestsListUseCase(questsRepository);
        deleteQuestsListUseCase = new DeleteQuestsListUseCase(questsRepository);
        deleteAllQuestsUseCase = new DeleteAllQuestsUseCase(questsRepository);
        setQuestsFilterStateUseCase = new SetQuestsFilterStateUseCase(questsRepository);
        setQuestsSortingStateUseCase = new SetQuestsSortingState(questsRepository);
        populateWithSamplesUseCase = new PopulateWithSamplesUseCase(questsRepository);
        setQuestCompletedUseCase = new SetQuestCompletedUseCase(questsRepository);
        setQuestImportantUseCase = new SetQuestImportantUseCase(questsRepository);
        updateQuestsDateDueStateUseCase = new UpdateQuestsDateDueStateUseCase(questsRepository);

        getQuestDateDueStateUseCase = new GetQuestDateDueStateUseCase();

        currentFilterState.setValue(getFilterStateUseCase.invoke());
        currentSortingState.setValue(getSortingStateUseCase.invoke());

        quests = Transformations.switchMap(currentFilterState, new Function<QuestsFilterState,
                LiveData<List<Quest>>>() {
            @Override
            public LiveData<List<Quest>> apply(QuestsFilterState filterType) {
                switch (currentFilterState.getValue()) {
                    case ALL:
                        return getAllQuestsUseCase.invoke();
                    case ACTIVE:
                        return getActiveQuestsUseCase.invoke();
                    case COMPLETED:
                        return getCompletedQuestsUseCase.invoke();
                    case IMPORTANT:
                        return getImportantQuestsUseCase.invoke();
                    default:
                        return null;

                }
            }
        });

        questsToShow.addSource(currentSortingState, new Observer<QuestsSortingState>() {
            @Override
            public void onChanged(QuestsSortingState questsSortingState) {
                if (quests.getValue() != null) {
                    switch (currentSortingState.getValue()) {
                        case NAME:
                            questsToShow.setValue(sortByNameUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DATE_DUE:
                            questsToShow.setValue(sortByDateDueUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DATE_ADDED:
                            questsToShow.setValue(sortByDateAddedUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                        case DIFFICULTY:
                            questsToShow.setValue(sortByDiffucultyUseCase.invoke((List<Quest>) questsToShow.getValue()));
                            break;
                    }
                }
            }
        });
        questsToShow.addSource(quests, new Observer<List<Quest>>() {
            @Override
            public void onChanged(List<Quest> quests) {
                if (quests != null) {
                    switch (currentSortingState.getValue()) {
                        case NAME:
                            questsToShow.setValue(sortByNameUseCase.invoke(quests));
                            break;
                        case DATE_DUE:
                            questsToShow.setValue(sortByDateDueUseCase.invoke(quests));
                            break;
                        case DATE_ADDED:
                            questsToShow.setValue(sortByDateAddedUseCase.invoke(quests));
                            break;
                        case DIFFICULTY:
                            questsToShow.setValue(sortByDiffucultyUseCase.invoke(quests));
                            break;
                    }
                }
            }
        });
    }

    LiveData<List<Quest>> getQuests() {
        return questsToShow;
    }

    LiveData<Boolean> isSelectionStarted() {
        return isSelectionStarted;
    }

    public void insert(List<Quest> questList) {
        insertQuestsListUseCase.invoke(questList);
    }

    void deleteAll() {
        deleteAllQuestsUseCase.invoke();
    }

    void populateWithSamples() {
        populateWithSamplesUseCase.invoke();
    }

    public void delete(List<Quest> questList) {
        deleteQuestsListUseCase.invoke(questList);
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

    void setQuestCompleted(int position, boolean isCompleted) {
        setQuestCompletedUseCase.invoke(getQuests().getValue().get(position), isCompleted);
    }

    void setQuestImportant(int position, boolean isImportant) {
        setQuestImportantUseCase.invoke(getQuests().getValue().get(position), isImportant);
    }

    void updateQuestsDateDueState() {
        if (getQuests().getValue() != null) {
            updateQuestsDateDueStateUseCase.invoke(getQuests().getValue());
        }
    }
}
