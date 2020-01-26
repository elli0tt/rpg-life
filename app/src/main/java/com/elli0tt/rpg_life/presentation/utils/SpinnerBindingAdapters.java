package com.elli0tt.rpg_life.presentation.utils;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.elli0tt.rpg_life.domain.model.Quest;

public class SpinnerBindingAdapters {

    @BindingAdapter(value = {"app:setSelectedItem", "app:selectedItemChanged"}, requireAll = false)
    public static void setSelectedItem(final AppCompatSpinner spinner,
                                       final @Quest.Difficulty int selectedItem,
                                       final InverseBindingListener changeListener){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeListener.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                changeListener.onChange();
            }
        });

        spinner.setSelection(selectedItem);
    }

    @InverseBindingAdapter(attribute = "app:setSelectedItem", event = "app:selectedItemChanged")
    public static @Quest.Difficulty int getSelectedItem(final AppCompatSpinner spinner){
        return spinner.getSelectedItemPosition();
    }

}
