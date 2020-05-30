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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddEditQuestFragment extends Fragment {
    private FragmentAddEditQuestBinding binding;

    private SubQuestsAdapter subQuestsAdapter;

    private NavController navController;

    private AddEditQuestViewModel viewModel;
    private AddEditQuestAddSkillToQuestSharedViewModel sharedViewModel;

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
        sharedViewModel =
                new ViewModelProvider(requireActivity()).get(AddEditQuestAddSkillToQuestSharedViewModel.class);
        binding = FragmentAddEditQuestBinding.inflate(inflater, container, false);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        setupSubQuestsRecycler();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            viewModel.start(AddEditQuestFragmentArgs.fromBundle(getArguments()).getQuestId(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getParentQuestId());
            //TODO: DELETE WHEN TREE SUBQUESTS ARE IMPLEMENTED
            if (AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest()) {
                binding.addSubquestButton.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.subquests_text_view).setVisibility(View.INVISIBLE);
            }
        }

        subscribeToViewModel();

        binding.nameEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
        binding.descriptionEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
        binding.addDateDueView.setOnClickListener(onAddDateDueViewClickListener);
        binding.addDateDueView.setOnRemoveClickListener(onRemoveDateDueViewClickListener);
        binding.repeatView.setOnClickListener(onRepeatViewClickListener);
        binding.repeatView.setOnRemoveClickListener(onRemoveRepeatViewClickListener);
        binding.addSubquestButton.setOnClickListener(onAddSubQuestButtonClickListener);
        binding.addSkillsButton.setOnClickListener(onAddSkillsButtonClickListener);
        binding.difficultyView.setOnClickListener(onDifficultyViewClickListener);
        binding.difficultyView.setOnRemoveClickListener(onRemoveDifficultyViewClickListener);
        binding.addStartDateView.setOnClickListener(onAddStartDateViewClickListener);
        binding.addStartDateView.setOnRemoveClickListener(onRemoveStartDateViewClickListener);
        binding.addStartTimeView.setOnClickListener(onAddStartTimeViewClickListener);
        binding.addStartTimeView.setOnRemoveClickListener(onRemoveStartTimeViewClickListener);
        binding.addTimeDueView.setOnClickListener(onAddTimeDueViewClickListener);
        binding.addTimeDueView.setOnRemoveClickListener(onRemoveTimeDueViewClickListener);

        if (viewModel.getIsNewQuest()) {
            binding.nameEditText.requestFocus();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        veryEasyTitle = getString(R.string.add_edit_quest_difficulty_very_easy,
                Difficulty.VERY_EASY.getXpIncrease());
        easyTitle = getString(R.string.add_edit_quest_difficulty_easy,
                Difficulty.EASY.getXpIncrease());
        normalTitle = getString(R.string.add_edit_quest_difficulty_normal,
                Difficulty.NORMAL.getXpIncrease());
        hardTitle = getString(R.string.add_edit_quest_difficulty_hard,
                Difficulty.HARD.getXpIncrease());
        veryHardTitle = getString(R.string.add_edit_quest_difficulty_very_hard,
                Difficulty.VERY_HARD.getXpIncrease());
        impossibleTitle = getString(R.string.add_edit_quest_difficulty_impossible,
                Difficulty.IMPOSSIBLE.getXpIncrease());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_edit_quest_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.saveQuest(sharedViewModel.getRelatedSkills().getValue());
    }

    private void subscribeToViewModel() {
        viewModel.getNameErrorMessageId().observe(getViewLifecycleOwner(),
                errorMessageId -> {
                    if (errorMessageId != null) {
                        binding.nameTextInput.setError(getString(errorMessageId));
                    }
                }
        );
        viewModel.getDateDueState().observe(getViewLifecycleOwner(),
                dateDueState -> {
                    switch (dateDueState) {
                        case NOT_SET:
                            binding.addDateDueView.setText(R.string.add_edit_quest_add_date_due);
                            binding.addDateDueView.setRemoveIconVisibility(View.INVISIBLE);
                            binding.addTimeDueView.setVisibility(View.GONE);
                            binding.addTimeDueView.setRemoveIconVisibility(View.INVISIBLE);
                            break;
                        case DATE_SET:
                            binding.addDateDueView.setText(viewModel.getDateDueFormatted());
                            binding.addDateDueView.setRemoveIconVisibility(View.VISIBLE);
                            binding.addTimeDueView.setVisibility(View.VISIBLE);
                            binding.addTimeDueView.setText(R.string.add_edit_quest_add_time_due);
                            break;
                        case DATE_TIME_SET:
                            binding.addDateDueView.setText(viewModel.getDateDueFormatted());
                            binding.addDateDueView.setRemoveIconVisibility(View.VISIBLE);
                            binding.addTimeDueView.setVisibility(View.VISIBLE);
                            binding.addTimeDueView.setText(viewModel.getTimeDueFormatted());
                            binding.addTimeDueView.setRemoveIconVisibility(View.VISIBLE);
                            break;
                    }
                });
        viewModel.getStartDateState().observe(getViewLifecycleOwner(), startDateState -> {
            switch (startDateState) {
                case NOT_SET:
                    binding.addStartDateView.setText(R.string.add_edit_quest_add_start_date);
                    binding.addStartDateView.setRemoveIconVisibility(View.INVISIBLE);
                    binding.addStartTimeView.setVisibility(View.GONE);
                    binding.addStartTimeView.setRemoveIconVisibility(View.INVISIBLE);
                    break;
                case DATE_SET:
                    binding.addStartDateView.setText(viewModel.getStartDateFormatted());
                    binding.addStartDateView.setRemoveIconVisibility(View.VISIBLE);
                    binding.addStartTimeView.setVisibility(View.VISIBLE);
                    binding.addStartTimeView.setText(R.string.add_edit_quest_add_start_time);
                    break;
                case DATE_TIME_SET:
                    binding.addStartDateView.setText(viewModel.getStartDateFormatted());
                    binding.addStartDateView.setRemoveIconVisibility(View.VISIBLE);
                    binding.addStartTimeView.setText(viewModel.getStartTimeFormatted());
                    binding.addStartTimeView.setRemoveIconVisibility(View.VISIBLE);
                    binding.addStartTimeView.setVisibility(View.VISIBLE);
                    break;
            }
        });
        viewModel.getRepeatTextResId().observe(getViewLifecycleOwner(),
                textResId -> binding.repeatView.setText(textResId));
        viewModel.getRepeatState().observe(getViewLifecycleOwner(),
                repeatState -> {
                    if (repeatState.equals(Quest.RepeatState.NOT_SET)) {
                        binding.repeatView.setRemoveIconVisibility(View.INVISIBLE);
                    } else {
                        binding.repeatView.setRemoveIconVisibility(View.VISIBLE);
                    }
                });
        viewModel.getSubQuests().observe(getViewLifecycleOwner(),
                subQuests -> subQuestsAdapter.submitList(subQuests));
        viewModel.getDifficulty().observe(getViewLifecycleOwner(), difficulty -> {
            switch (difficulty) {
                case VERY_EASY:
                    binding.difficultyView.setText(veryEasyTitle);
                    break;
                case EASY:
                    binding.difficultyView.setText(easyTitle);
                    break;
                case NORMAL:
                    binding.difficultyView.setText(normalTitle);
                    break;
                case HARD:
                    binding.difficultyView.setText(hardTitle);
                    break;
                case VERY_HARD:
                    binding.difficultyView.setText(veryHardTitle);
                    break;
                case IMPOSSIBLE:
                    binding.difficultyView.setText(impossibleTitle);
                    break;
            }
            if (difficulty.equals(Difficulty.NOT_SET)) {
                binding.difficultyView.setRemoveIconVisibility(View.INVISIBLE);
            } else {
                binding.difficultyView.setRemoveIconVisibility(View.VISIBLE);
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
        binding.subquestsRecycler.addItemDecoration(new DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL));
        binding.subquestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        binding.subquestsRecycler.setAdapter(subQuestsAdapter);
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
        binding.difficultyView.setText(R.string.add_difficulty);
        viewModel.removeDifficulty();
    };

    private View.OnClickListener onAddStartDateViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showAddStartDatePopupMenu(v);
    };

    private View.OnClickListener onRemoveStartDateViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeStartDate();
    };

    private View.OnClickListener onAddStartTimeViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        pickStartTime();
    };

    private View.OnClickListener onRemoveStartTimeViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeStartTime();
    };

    private View.OnClickListener onAddTimeDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        pickTimeDue();
    };

    private View.OnClickListener onRemoveTimeDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeTimeDue();
    };

    private void showAddStartDatePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.add_edit_quest_add_start_date_popup_menu,
                popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_edit_quest_add_start_date_popup_today:
                    viewModel.setStartDateToday();
                    return true;
                case R.id.add_edit_quest_add_start_date_popup_tomorrow:
                    viewModel.setStartDateTomorrow();
                    return true;
                case R.id.add_edit_quest_add_start_date_popup_next_week:
                    viewModel.setStartDateNextWeek();
                    return true;
                case R.id.add_edit_quest_add_start_date_popup_pick_date:
                    pickStartDate();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void pickStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    viewModel.setStartDate(year, month, dayOfMonth);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void pickStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> viewModel.setStartTime(hourOfDay, minute),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void showAddDateDuePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
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
                    pickDateDue();
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void pickDateDue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    viewModel.setDateDue(year, month, dayOfMonth);
                },
                GregorianCalendar.getInstance().get(Calendar.YEAR),
                GregorianCalendar.getInstance().get(Calendar.MONTH),
                GregorianCalendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void pickTimeDue() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minute) -> viewModel.setDateDue(hourOfDay, minute),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void showRepeatPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
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
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
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
            binding.difficultyView.setText(item.getTitle().toString());
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