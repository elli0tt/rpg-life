package com.elli0tt.rpg_life.presentation.add_edit_quest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentAddEditQuestBinding;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.custom_view.ButtonWithRemoveIcon;
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditQuestFragment extends Fragment {
    private EditText nameEditText;
    private EditText descriptionEditText;
    private TextInputLayout nameTextInput;
    private ButtonWithRemoveIcon addDateDueView;
    private ButtonWithRemoveIcon repeatView;
    private RecyclerView subQuestsRecycler;
    private Button addSubQuestButton;
    private Button addSkillsButton;
    private RecyclerView skillsRecycler;
    private ButtonWithRemoveIcon difficultyView;

    private SubQuestsAdapter subQuestsAdapter;

    private NavController navController;

    private AddEditQuestViewModel viewModel;

    private String veryEasyTitle;
    private String easyTitle;
    private String normalTitle;
    private String hardTitle;
    private String veryHardTitle;
    private String impossibleTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(AddEditQuestViewModel.class);
        FragmentAddEditQuestBinding binding =
                FragmentAddEditQuestBinding.inflate(inflater, container, false);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.name_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        nameTextInput = view.findViewById(R.id.name_text_input);
        addDateDueView = view.findViewById(R.id.add_date_due_view);
        repeatView = view.findViewById(R.id.repeat_view);
        subQuestsRecycler = view.findViewById(R.id.subquests_recycler);
        addSubQuestButton = view.findViewById(R.id.add_subquest_button);
        addSkillsButton = view.findViewById(R.id.add_skills_button);
        skillsRecycler = view.findViewById(R.id.skills_recycler);
        difficultyView = view.findViewById(R.id.difficulty_view);

        navController = NavHostFragment.findNavController(this);

        setupSubQuestsRecycler();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            viewModel.start(AddEditQuestFragmentArgs.fromBundle(getArguments()).getQuestId(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getParentQuestId());
            //TODO: DELETE WHEN TREE SUBQUESTS ARE IMPLEMENTED
            if (AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest()) {
                addSubQuestButton.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.subquests_text_view).setVisibility(View.INVISIBLE);
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
        addSkillsButton.setOnClickListener(onAddSkillsButtonClickListener);
        difficultyView.setOnClickListener(onDifficultyViewClickListener);
        difficultyView.setOnRemoveClickListener(onRemoveDifficultyViewClickListener);

        if (viewModel.getIsNewQuest()) {
            nameEditText.requestFocus();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        veryEasyTitle = getString(R.string.add_edit_quest_difficulty_very_easy,
                Quest.Difficulty.VERY_EASY.getXpIncrease());
        easyTitle = getString(R.string.add_edit_quest_difficulty_easy,
                Quest.Difficulty.EASY.getXpIncrease());
        normalTitle = getString(R.string.add_edit_quest_difficulty_normal,
                Quest.Difficulty.NORMAL.getXpIncrease());
        hardTitle = getString(R.string.add_edit_quest_difficulty_hard,
                Quest.Difficulty.HARD.getXpIncrease());
        veryHardTitle = getString(R.string.add_edit_quest_difficulty_very_hard,
                Quest.Difficulty.VERY_HARD.getXpIncrease());
        impossibleTitle = getString(R.string.add_edit_quest_difficulty_impossible,
                Quest.Difficulty.IMPOSSIBLE.getXpIncrease());
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
                        addDateDueView.setRemoveIconVisibility(View.INVISIBLE);
                    } else {
                        addDateDueView.setText(viewModel.getDueDateFormatted());
                        addDateDueView.setRemoveIconVisibility(View.VISIBLE);
                    }
                });
        viewModel.getRepeatTextResId().observe(getViewLifecycleOwner(),
                textResId -> repeatView.setText(textResId));
        viewModel.getRepeatState().observe(getViewLifecycleOwner(),
                repeatState -> {
                    if (repeatState.equals(Quest.RepeatState.NOT_SET)) {
                        repeatView.setRemoveIconVisibility(View.INVISIBLE);
                    } else {
                        repeatView.setRemoveIconVisibility(View.VISIBLE);
                    }
                });
        viewModel.getSubQuests().observe(getViewLifecycleOwner(),
                subQuests -> subQuestsAdapter.submitList(subQuests));
        viewModel.getDifficulty().observe(getViewLifecycleOwner(), difficulty -> {
            switch (difficulty) {
                case VERY_EASY:
                    difficultyView.setText(veryEasyTitle);
                    break;
                case EASY:
                    difficultyView.setText(easyTitle);
                    break;
                case NORMAL:
                    difficultyView.setText(normalTitle);
                    break;
                case HARD:
                    difficultyView.setText(hardTitle);
                    break;
                case VERY_HARD:
                    difficultyView.setText(veryHardTitle);
                    break;
                case IMPOSSIBLE:
                    difficultyView.setText(impossibleTitle);
                    break;
            }
        });
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
    }

    private void setupSkillsRecycler() {
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

    private View.OnFocusChangeListener onEditTextsFocusChangeListener = (view, hasFocus) -> {
        if (hasFocus) {
            SoftKeyboardUtil.showKeyboard(view, getActivity());
        } else {
            SoftKeyboardUtil.hideKeyboard(view, getActivity());
        }
    };

    private View.OnClickListener onAddDateDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showAddDateDuePopupMenu(v);
    };

    private View.OnClickListener onRemoveDateDueViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SoftKeyboardUtil.hideKeyboard(v, getActivity());
            viewModel.removeDateDue();
        }
    };

    private View.OnClickListener onRepeatViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showRepeatPopup(v);
    };

    private View.OnClickListener onRemoveRepeatViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SoftKeyboardUtil.hideKeyboard(v, getActivity());
            viewModel.removeRepeat();
        }
    };

    private View.OnClickListener onAddSubQuestButtonClickListener =
            v -> navigateToAddSubQuestScreen();

    private View.OnClickListener onAddSkillsButtonClickListener = v -> {
        navigateToAddSkillsToQuestScreen();
    };

    private View.OnClickListener onDifficultyViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showDifficultyPopupMenu(v);
    };

    private View.OnClickListener onRemoveDifficultyViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        difficultyView.setText(R.string.add_difficulty);
    };

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

    private void showDifficultyPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        Menu menu = popupMenu.getMenu();

        menu.add(Menu.NONE, Constants.VERY_EASY_POPUP_MENU_ITEM_ID,
                Constants.VERY_EASY_POPUP_MENU_ITEM_ORDER, veryEasyTitle);
        menu.add(Menu.NONE, Constants.EASY_POPUP_MENU_ITEM_ID, Constants.EASY_POPUP_MENU_ITEM_ORDER,
                easyTitle);
        menu.add(Menu.NONE, Constants.NORMAL_POPUP_MENU_ITEM_ID,
                Constants.NORMAL_POPUP_MENU_ITEM_ORDER, normalTitle);
        menu.add(Menu.NONE, Constants.HARD_POPUP_MENU_ITEM_ID, Constants.HARD_POPUP_MENU_ITEM_ORDER,
                hardTitle);
        menu.add(Menu.NONE, Constants.VERY_HARD_POPUP_MENU_ITEM_ID,
                Constants.VERY_HARD_POPUP_MENU_ITEM_ORDER, veryHardTitle);
        menu.add(Menu.NONE, Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ID,
                Constants.IMPOSSIBLE_POPUP_MENU_ITEM_ORDER, impossibleTitle);

        popupMenu.setOnMenuItemClickListener(item -> {
            difficultyView.setText(item.getTitle().toString());
            difficultyView.setRemoveIconVisibility(View.VISIBLE);
            viewModel.changeDifficulty(item.getItemId());
            return true;
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

    private void navigateToAddSkillsToQuestScreen() {
        AddEditQuestFragmentDirections.ActionAddEditQuestScreenToAddSkillsToQuestFragment action =
                AddEditQuestFragmentDirections.actionAddEditQuestScreenToAddSkillsToQuestFragment();
        action.setQuestId(viewModel.getQuestId());
        navController.navigate(action);
    }
}
