package com.elli0tt.rpg_life.presentation.character;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.CharacteristicsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Characteristic;

import java.util.ArrayList;
import java.util.List;

public class CharacteristicsViewModel extends AndroidViewModel {
    private CharacteristicsRepositoryImpl characteristicsRepository;
    private LiveData<List<Characteristic>> allCharacteristics;

    public CharacteristicsViewModel(@NonNull Application application) {
        super(application);
        characteristicsRepository = new CharacteristicsRepositoryImpl(application);
        allCharacteristics = characteristicsRepository.getAllCharacteristics();
    }

    public LiveData<List<Characteristic>> getAllCharacteristics() {
        return allCharacteristics;
    }

    public void insert(Characteristic characteristic){
        characteristicsRepository.insert(characteristic);
    }

    private List<Characteristic> generateSampleCharacteristicsList() {
        List<Characteristic> resultList = new ArrayList<>();
        resultList.add(new Characteristic("Strength"));
        resultList.add(new Characteristic("Intelligence"));
        resultList.add(new Characteristic("Agility"));
        resultList.add(new Characteristic("Endurance"));
        resultList.add(new Characteristic("Willpower"));
        resultList.add(new Characteristic("Procrastination"));
        resultList.add(new Characteristic("Self-confidence"));
        resultList.add(new Characteristic("Communication"));
        return resultList;
    }

    public void populateWithSamples(){
        characteristicsRepository.insert(generateSampleCharacteristicsList());
    }
}
