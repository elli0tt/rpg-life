package com.elli0tt.rpg_life.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.elli0tt.rpg_life.domain.modal.Characteristic;

import java.util.List;

@Dao
public interface CharacteristicsDao {

    @Insert
    void insert(Characteristic characteristic);
    @Insert
    void insert(List<Characteristic> characteristicList);

//    @Delete
//    void delete();
//
//    @Update
//    void update();

    @Query("SELECT * from characteristics_table ORDER BY id")
    LiveData<List<Characteristic>> getAllCharacteristics();

}
