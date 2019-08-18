package com.elli0tt.rpg_life.presentation.character;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.data.repository.CharacteristicsRepositoryImpl;
import com.elli0tt.rpg_life.domain.modal.Characteristic;

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

    public void insert(List<Characteristic> characteristicList){
        characteristicsRepository.insert(characteristicList);
    }
}
