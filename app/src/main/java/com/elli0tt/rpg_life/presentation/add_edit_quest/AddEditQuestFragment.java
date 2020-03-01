package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentAddEditQuestBinding;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.custom_view.ButtonWithRemoveIcon;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class AddEditQuestFragment extends Fragment {
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner difficultySpinner;
    private TextInputLayout nameTextInput;
    private ButtonWithRemoveIcon addDateDueView;
    private ButtonWithRemoveIcon repeatView;
    private RecyclerView subQuestsRecycler;
    private Button addSubQuestButton;


    private SubQuestsAdapter subQuestsAdapter;

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
        addDateDueView = view.findViewById(R.id.add_edit_quest_add_date_due_view);
        repeatView = view.findViewById(R.id.add_edit_quest_repeat_view);
        subQuestsRecycler = view.findViewById(R.id.add_edit_quest_subquests_recycler);
        addSubQuestButton = view.findViewById(R.id.add_edit_quest_add_subquest_button);

        navController = NavHostFragment.findNavController(this);

        setupDifficultySpinner();
        setupSubQuestsRecycler();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            viewModel.start(AddEditQuestFragmentArgs.fromBundle(getArguments()).getQuestId(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getParentQuestId());
            //TODO: DELETE WHEN TREE SUBQUESTS ARE IMPLEMENTED
            if (AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest()){
                addSubQuestButton.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.add_edit_quest_subquests_text_view).setVisibility(View.INVISIBLE);
            }
        }

        subscribeToViewModel();

        nameEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
        descriptionEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
        addDateDueView.setOnClickListener(onAddDateDueViewClickListener);
        addDateDueView.setOnRemoveClickListener(onRemoveDateDueViewClickListener);
        repeatView.setOnClickListener(onRepeatViewClickListener);
        repeatView.setOnRemoveClickListener(onRemoveRepeatViewClickListener);
        addSubQuestButton.setOnClickListener(onAddSubQuestButtonClickListener);
    }

    private void subscribeToViewModel() {
        viewModel.getNameErrorMessageId().observe(getViewLifecycleOwner(),
                errorMessageId -> {
                    if (errorMessageId != null) {
                        nameTextInput.setError(getString(errorMessageId));
                    }
                }
        );
        viewModel.isDateDueSet().observe(getViewLifecycleOwner(),
                isDateDueSet -> {
                    if (!isDateDueSet) {
                        addDateDueView.setText(R.string.add_edit_quest_add_date_due);
                        addDateDueView.setRemoveIconVisisbility(View.INVISIBLE);
                    } else {
                        addDateDueView.setText(viewModel.getDueDateFormatted());
                        addDateDueView.setRemoveIconVisisbility(View.VISIBLE);
                    }
                });
        viewModel.getRepeatTextResId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer textResId) {
                repeatView.setText(textResId);
            }
        });
        viewModel.getRepeatState().observe(getViewLifecycleOwner(),
                new Observer<Quest.RepeatState>() {
                    @Override
                    public void onChanged(Quest.RepeatState repeatState) {
                        if (repeatState.equals(Quest.RepeatState.NOT_SET)) {
                            repeatView.setRemoveIconVisisbility(View.INVISIBLE);
                        } else {
                            repeatView.setRemoveIconVisisbility(View.VISIBLE);
                        }
                    }
                });
        viewModel.getSubQuests().observe(getViewLifecycleOwner(), new Observer<List<Quest>>() {
            @Override
            public void onChanged(List<Quest> subQuests) {
                subQuestsAdapter.submitList(subQuests);
            }
        });
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
            difficultySpinner.setOnTouchListener((v, event) -> {
                AddEditQuestFragment.this.hideKeyboard(v);
                v.performClick();
                return false;
            });
        }
    }

    private void setupSubQuestsRecycler() {
        subQuestsAdapter = new SubQuestsAdapter();
        subQuestsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) -> {
            viewModel.completeSubQuest(position, isCompleted);
        });
        subQuestsAdapter.setOnRemoveButtonClickListener(position -> {
            viewModel.removeSubQuest(position);
        });
        subQuestsRecycler.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        subQuestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        subQuestsRecycler.setAdapter(subQuestsAdapter);

//        List<Quest> list = new ArrayList<>(10);
//        for (int i = 0; i < 20; ++i){
//            list.add(new Quest("Subquest " + i));
//        }
//        subQuestsAdapter.submitList(list);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
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

    private View.OnClickListener onAddDateDueViewClickListener = v -> {
        hideKeyboard(v);
        showAddDateDuePopupMenu(v);
    };

    private View.OnClickListener onRemoveDateDueViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v);
            viewModel.removeDateDue();
        }
    };

    private View.OnClickListener onRepeatViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v);
            showRepeatPopup(v);
        }
    };

    private View.OnClickListener onRemoveRepeatViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard(v);
            viewModel.removeRepeat();
        }
    };

    private View.OnClickListener onAddSubQuestButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigateToAddSubQuestScreen();
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

    private void showAddDateDuePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.add_edit_quest_add_date_due_popup_menu,
                popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_edit_quest_add_date_due_popup_today:
                    viewModel.setDateDueToday();
                    return true;
                case R.id.add_edit_quest_add_date_due_popup_tomorrow:
                    viewModel.setDateDueTomorrow();
                    return true;
                case R.id.add_edit_quest_add_date_due_popup_next_week:
                    viewModel.setDateDueNextWeek();
                    return true;
                case R.id.add_edit_quest_add_date_due_popup_pick_date:
                    pickDate();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();

    }

    private void pickDate() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> viewModel.setDateDue(hourOfDay,
                        minute),
                viewModel.getCurrentHourOfDay(), viewModel.getCurrentMinute(), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    viewModel.setDateDue(year, month, dayOfMonth);
                    timePickerDialog.show();
                }, viewModel.getCurrentYear(), viewModel.getCurrentMonth(),
                viewModel.getCurrentDayOfMonth());
        datePickerDialog.show();
    }

    private void showRepeatPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.add_edit_quest_repeate_popup_menu,
                popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_edit_quest_repeat_popup_daily:
                    viewModel.setRepeatState(Quest.RepeatState.DAILY);
                    return true;
                case R.id.add_edit_quest_repeat_popup_weekdays:
                    viewModel.setRepeatState(Quest.RepeatState.WEEKDAYS);
                    return true;
                case R.id.add_edit_quest_repeat_popup_weekends:
                    viewModel.setRepeatState(Quest.RepeatState.WEEKENDS);
                    return true;
                case R.id.add_edit_quest_repeat_popup_weekly:
                    viewModel.setRepeatState(Quest.RepeatState.WEEKLY);
                    return true;
                case R.id.add_edit_quest_repeat_popup_monthly:
                    viewModel.setRepeatState(Quest.RepeatState.MONTHLY);
                    return true;
                case R.id.add_edit_quest_repeat_popup_yearly:
                    viewModel.setRepeatState(Quest.RepeatState.YEARLY);
                    return true;
                case R.id.add_edit_quest_repeat_popup_custom:

                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void navigateToAddSubQuestScreen() {
        AddEditQuestFragmentDirections.ActionAddEditQuestScreenSelf action =
                AddEditQuestFragmentDirections.actionAddEditQuestScreenSelf();
        action.setParentQuestId(viewModel.getQuestId());
        action.setIsSubQuest(true);
        navController.navigate(action);
    }
}
