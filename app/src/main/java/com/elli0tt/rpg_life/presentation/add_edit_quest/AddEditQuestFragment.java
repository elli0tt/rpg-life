package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentAddEditQuestBinding;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.google.android.material.textfield.TextInputLayout;


public class AddEditQuestFragment extends Fragment {
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner difficultySpinner;
    private TextInputLayout nameTextInput;

    private NavController navController;

    private AddEditQuestViewModel viewModel;

    //Tags for Log.d()
    private static final String ON_FOCUS_CHANGE_TAG = "On focus change";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this).get(AddEditQuestViewModel.class);
        FragmentAddEditQuestBinding binding =
                FragmentAddEditQuestBinding.inflate(inflater, container, false);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.add_edit_quest_name_edit_text);
        descriptionEditText = view.findViewById(R.id.add_edit_quest_description_edit_text);
        difficultySpinner = view.findViewById(R.id.add_edit_quest_difficulty_spinner);
        nameTextInput = view.findViewById(R.id.add_edit_quest_name_text_input);

        navController = NavHostFragment.findNavController(this);

        setupDifficultySpinner();
        setHasOptionsMenu(true);

        viewModel.getNameErrorMessage().observe(this, s -> nameTextInput.setError(s));

        viewModel.start(AddEditQuestFragmentArgs.fromBundle(getArguments()).getQuestId());

        nameEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
        descriptionEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
    }

    private void setupDifficultySpinner() {
        Activity activity = getActivity();
        if (activity != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    activity, R.array.difficulty_levels_texts,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            difficultySpinner.setAdapter(adapter);
            difficultySpinner.setSelection(Quest.NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_quest_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_quest_confirm_button:
                if (viewModel.saveQuest()) {
                    navController.popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private View.OnFocusChangeListener onEditTextsFocusChangeListener = (v, hasFocus) -> {
        Log.d(ON_FOCUS_CHANGE_TAG, "on focus change called");
        if (!hasFocus) {
            Log.d(ON_FOCUS_CHANGE_TAG, " on try close keyboard");
            hideKeyboard(v);
        }
    };

    private void hideKeyboard(View view) {
        Activity activity = getActivity();
        if (activity != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
