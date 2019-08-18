package com.elli0tt.rpg_life.presentation.quests;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

public abstract class BaseAddEditQuestFragment extends Fragment {

    protected @BindView(R.id.add_edit_quest_name_edit_text) EditText nameEditText;
    protected @BindView(R.id.add_edit_quest_description_edit_text) EditText descriptionEditText;
    protected @BindView(R.id.add_edit_quest_difficulty_spinner) Spinner difficultySpinner;

    protected NavController navController;

    private static final String ON_FOCUS_CHANGE_TAG = "On focus change";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_quest, container, false);
        ButterKnife.bind(this, view);

        navController = NavHostFragment.findNavController(this);
        setupDifficultySpinner();
        setHasOptionsMenu(true);
        return view;
    }

    private void setupDifficultySpinner() {
        Activity activity = getActivity();
        if (activity != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    activity, R.array.difficulty_levels_texts, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            difficultySpinner.setAdapter(adapter);
            difficultySpinner.setSelection(Quest.NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_quest_toolbar_menu, menu);
    }


    @OnFocusChange({R.id.add_edit_quest_name_edit_text, R.id.add_edit_quest_description_edit_text})
    void onEditTextsFocusChange(View v, boolean hasFocus) {
        Log.d(ON_FOCUS_CHANGE_TAG, "on focus change called");
        if (!hasFocus) {
            Log.d(ON_FOCUS_CHANGE_TAG, " on try close keyboard");
            hideKeyboard(v);
        }
    }

    private void hideKeyboard(View view) {
        Activity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}