package com.elli0tt.rpg_life.presentation.quests;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.ArrayList;
import java.util.List;

public class QuestsViewModel extends AndroidViewModel {
    private QuestsRepositoryImpl repository;

    private LiveData<List<Quest>> quests;

    private MutableLiveData<QuestsFilterType> currentFilter =
            new MutableLiveData<>(QuestsFilterType.ALL);

    private MutableLiveData<List<Quest>> questsToShow ;
//    =
//            Transformations.switchMap(currentFilter, new Function<LiveData<List<Quest>>, MutableLiveData<List<Quest>>>() {
//
//
//                @Override
//                public MutableLiveData<List<Quest>> apply(LiveData<List<Quest>> input) {
////                    QuestsFilterType filter = currentFilter.getValue();
////                    if (filter == QuestsFilterType.ALL) {
////                        return repository.getAllQuests();
////                    } else if (filter == QuestsFilterType.ACTIVE){
////                        return repository.getActiveQuests();
////                    } else{
////                        return repository.getCompletedQuests();
////                    }
//
//                }
//
//
//            });




    public QuestsViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestsRepositoryImpl(application);
        loadQuests();
        //quests = repository.getAllQuests();
        //questsToShow.setValue(quests.getValue());
    }

    LiveData<List<Quest>> getQuests() {
        return questsToShow;
    }

    public void insert(List<Quest> questList) {
        repository.insert(questList);
    }

    void deleteAll() {
        repository.deleteAll();
    }

    void update(Quest quest) {
        repository.update(quest);
    }

    private List<Quest> generateSampleQuestsList() {
        List<Quest> result = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            result.add(new Quest("Quest " + i));
        }
        return result;
    }

    void populateWithSamples() {
        repository.insert(generateSampleQuestsList());
    }

    public void delete(List<Quest> questList) {
        repository.delete(questList);
    }

    public void setFiltering(QuestsFilterType filterType){
        //currentFilter = filterType;
        loadQuests();

        //questsToShow.setValue(getQuestsByFilter(filterType));

//        switch (currentFilter){
//            case ALL:
//                quests = repository.getAllQuests();
//                break;
//            case ACTIVE:
//                quests = repository.getActiveQuests();
//                break;
//            case COMPLETED:
//                quests = repository.getCompletedQuests();
//                break;
//        }
    }



//    private List<Quest> getQuestsByFilter(QuestsFilterType filterType){
//        List<Quest> questsByFilter = new ArrayList<>();
//        switch (filterType){
//            case ALL:
//                questsByFilter = quests.getValue();
//                break;
//            case ACTIVE:
//                for (Quest quest : quests.getValue()){
//                    if (!quest.isCompleted()) {
//                        questsByFilter.add(quest);
//                    }
//                }
//                break;
//            case COMPLETED:
//                for (Quest quest : quests.getValue()){
//                    if (quest.isCompleted()) {
//                        questsByFilter.add(quest);
//                    }
//                }
//                break;
//        }
//        return questsByFilter;
//    }

    private void loadQuests(){
//        List<Quest> quests = repository.getAllQuests();
//        List<Quest> result = new ArrayList<>();
//        for (Quest quest : quests){
//            switch(currentFilter){
//                case ALL:
//                    result.add(quest);
//                    break;
//                case ACTIVE:
//                    if (!quest.isCompleted()){
//                        result.add(quest);
//                    }
//                    break;
//                case COMPLETED:
//                    if (quest.isCompleted()){
//                        result.add(quest);
//                    }
//                    break;
//            }
//        }
//        questsToShow.setValue(result);

    }

}
