package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Characteristic;

import java.util.List;

public interface CharacteristicsRepository {
    LiveData<List<Characteristic>> getAllCharacteristics();

    void insertCharacteristics(Characteristic characteristic);

    void insertCharacteristics(List<Characteristic> characteristicList);

    void deleteCharacteristic();

    void updateCharacteristic();
}
