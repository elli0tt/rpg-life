package com.elli0tt.rpg_life.presentation.add_characteristic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.CharacteristicsRepositoryImpl;

public class AddEditCharacteristicViewModel extends AndroidViewModel {
    private CharacteristicsRepositoryImpl repository;

    private MutableLiveData<String> name = new MutableLiveData<>();

    public AddEditCharacteristicViewModel(@NonNull Application application) {
        super(application);
        repository = new CharacteristicsRepositoryImpl(application);
    }

    public LiveData<String> getName(){
        return name;
    }

    public void setName(String name){
        this.name.setValue(name);
    }
}