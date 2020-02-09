package com.elli0tt.rpg_life.domain.repository;

import androidx.lifecycle.LiveData;

import com.elli0tt.rpg_life.domain.model.Characteristic;

import java.util.List;

public interface CharacteristicsRepository {
    LiveData<List<Characteristic>> getAllCharacteristics();

    void insert(Characteristic characteristic);

    void insert(List<Characteristic> characteristicList);

    void delete();

    void update();
}
