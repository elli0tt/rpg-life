package com.elli0tt.rpg_life.presentation.add_characteristic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elli0tt.rpg_life.R;

public class AddEditCharacteristicFragment extends Fragment {

    private AddEditCharacteristicViewModel addEditCharacteristicViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_characteristic, container, false);


        return view;
    }
}
