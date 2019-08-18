package com.elli0tt.rpg_life.domain.repository;

import com.elli0tt.rpg_life.domain.modal.Characteristic;

import java.util.List;

public interface CharacteristicsRepository {
    //List<Characteristic> getAllCharacteristics();
    void insert(Characteristic characteristic);
    void insert(List<Characteristic> characteristics);
    void delete();
    void update();
}
