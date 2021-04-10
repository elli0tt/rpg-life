package com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elli0tt.rpg_life.domain.repository.UserRepository;

import javax.inject.Inject;

public class AddEditCharacteristicViewModel extends ViewModel {
    private final UserRepository userRepository;

    private final MutableLiveData<String> name = new MutableLiveData<>();

    @Inject
    public AddEditCharacteristicViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }
}
