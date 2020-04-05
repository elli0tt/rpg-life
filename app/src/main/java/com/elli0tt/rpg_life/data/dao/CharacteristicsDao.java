package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.elli0tt.rpg_life.domain.model.Characteristic;

import java.util.List;

@Dao
public interface CharacteristicsDao {

    @Insert
    void insert(Characteristic characteristic);

    @Insert
    void insert(List<Characteristic> characteristicList);

    @Query("SELECT * from characteristics_table ORDER BY id")
    LiveData<List<Characteristic>> getAllCharacteristics();

}
