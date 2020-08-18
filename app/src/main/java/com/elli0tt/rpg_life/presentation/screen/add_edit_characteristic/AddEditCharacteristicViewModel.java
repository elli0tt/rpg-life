package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.elli0tt.rpg_life.data.repository.CharacterRepositoryImpl;
import com.elli0tt.rpg_life.domain.repository.CharacterRepository;

public class AddEditCharacteristicViewModel extends AndroidViewModel {
    private CharacterRepository repository;

    private MutableLiveData<String> name = new MutableLiveData<>();

    public AddEditCharacteristicViewModel(@NonNull Application application) {
        super(application);
        repository = new CharacterRepositoryImpl(application);
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
}
