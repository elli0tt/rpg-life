package com.elli0tt.rpg_life.presentation.screen.add_edit_quest;

import static android.Manifest.permission.WRITE_CALENDAR;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentAddEditQuestBinding;
import com.elli0tt.rpg_life.domain.model.Difficulty;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.elli0tt.rpg_life.presentation.adapter.subquests.SubQuestsAdapter;
import com.elli0tt.rpg_life.presentation.broadcast_receiver.QuestReminderBroadcastReceiver;
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment;
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di.AddEditQuestComponent;
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import javax.inject.Inject;

public class AddEditQuestFragment extends BaseFragment {

    public static final String EXTRA_REMINDER_TITLE = "com.elli0tt.rpg_life.presentation" +
            ".add_edit_quest_extra_reminder_title";
    public static final String EXTRA_NOTIFICATION_ID = "com.elli0tt.rpg_life.presentation" +
            ".add_edit_quest_extra_notification_id";

    private static final int REQUEST_PERMISSIONS = 1000;
    private final View.OnFocusChangeListener onEditTextsFocusChangeListener = (view, hasFocus) -> {
//        if (hasFocus) {
//            SoftKeyboardUtil.showKeyboard(view, getActivity());
//        } else {
//            SoftKeyboardUtil.hideKeyboard(view, getActivity());
//        }
    };
    private final View.OnClickListener onAddStartTimeViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        pickTime(onStartTimeSetListener);
    };
    private final View.OnClickListener onAddTimeDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        pickTime(onTimeDueSetListener);
    };
    private final View.OnClickListener onAddReminderViewClickListener = v ->
            pickDate(onReminderDateSetListener);
    private final View.OnClickListener onRemoveReminderViewClickListener = v -> {
        //do nothing
    };
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private AddEditQuestComponent addEditQuestComponent;
    private FragmentAddEditQuestBinding binding;
    private SubQuestsAdapter subQuestsAdapter;
    private NavController navController;
    private AddEditQuestViewModel viewModel;
    private final TimePickerDialog.OnTimeSetListener onStartTimeSetListener =
            (view, hourOfDay, minute) -> viewModel.setStartTime(hourOfDay, minute);
    private final TimePickerDialog.OnTimeSetListener onTimeDueSetListener =
            (view, hourOfDay, minute) -> viewModel.setDateDue(hourOfDay, minute);
    private final TimePickerDialog.OnTimeSetListener onReminderTimeSetListener = (view, hourOfDay,
                                                                                  minute) -> {
        viewModel.setReminderTime(hourOfDay, minute);
        setReminderAlarm();
    };
    private final DatePickerDialog.OnDateSetListener onReminderDateSetListener = (view, year, month,
                                                                                  dayOfMonth) -> {
        viewModel.setReminderDate(year, month, dayOfMonth);
        pickTime(onReminderTimeSetListener);
    };
    private final View.OnClickListener onRemoveDateDueViewClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoftKeyboardUtil.hideKeyboard(v, getActivity());
                    viewModel.removeDateDue();
                }
            };
    private final View.OnClickListener onRepeatViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showRepeatPopup(v);
    };
    private final View.OnClickListener onRemoveRepeatViewClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoftKeyboardUtil.hideKeyboard(v, getActivity());
                    viewModel.removeRepeat();
                }
            };
    private final DatePickerDialog.OnDateSetListener onStartDateSetListener =
            (view, year, month, dayOfMonth) -> viewModel.setStartDate(year, month, dayOfMonth);
    private final DatePickerDialog.OnDateSetListener onDateDueSetListener =
            (view, year, month, dayOfMonth) -> viewModel.setDateDue(year, month, dayOfMonth);
    private final View.OnClickListener onAddDateDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showAddDateDuePopupMenu(v);
    };

    private final View.OnClickListener onAddSubQuestButtonClickListener =
            v -> viewModel.insertEmptyQuest();
    private final View.OnClickListener onAddSkillsButtonClickListener = v ->
            navigateToAddSkillsToQuestScreen();
    private final View.OnClickListener onRemoveDifficultyViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        binding.difficultyView.setText(R.string.add_difficulty);
        viewModel.removeDifficulty();
    };
    private final View.OnClickListener onAddStartDateViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showAddStartDatePopupMenu(v);
    };
    private final View.OnClickListener onRemoveStartDateViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeStartDate();
    };
    private final View.OnClickListener onRemoveStartTimeViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeStartTime();
    };
    private final View.OnClickListener onRemoveTimeDueViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        viewModel.removeTimeDue();
    };
    private final View.OnClickListener onAddToCalendarClickListener = v -> checkPermissions();
    private String veryEasyTitle;
    private String easyTitle;
    private String normalTitle;
    private String hardTitle;
    private String veryHardTitle;
    private String impossibleTitle;
    private final View.OnClickListener onDifficultyViewClickListener = v -> {
        SoftKeyboardUtil.hideKeyboard(v, getActivity());
        showDifficultyPopupMenu(v);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        initDagger();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(AddEditQuestViewModel.class);

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
        binding.nameEditText.clearFocus();

        if (getArguments() != null) {
            viewModel.start(
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getQuestId(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest(),
                    AddEditQuestFragmentArgs.fromBundle(getArguments()).getParentQuestId(),
                    getString(R.string.quest_date_due_today),
                    getString(R.string.quest_date_due_tomorrow)
            );
            //TODO: DELETE WHEN TREE SUBQUESTS ARE IMPLEMENTED
            if (AddEditQuestFragmentArgs.fromBundle(getArguments()).getIsSubQuest()) {
                binding.addSubquestButton.setVisibility(View.INVISIBLE);
            }
        }

        setupToolbar();
        initDifficultyToolbars();

        subscribeToViewModel();
        setListeners();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_edit_quest_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navController.popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.saveQuest();
    }

    private void initDagger() {
        addEditQuestComponent = getAppComponent().addEditQuestComponentFactory().create();
        addEditQuestComponent.inject(this);
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);
        ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDifficultyToolbars() {
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

    private void setListeners() {
        binding.nameEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
//        binding.descriptionEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);
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
        binding.addToCalendarButton.setOnClickListener(onAddToCalendarClickListener);
        binding.addReminderView.setOnClickListener(onAddReminderViewClickListener);
        binding.addReminderView.setOnRemoveClickListener(onRemoveReminderViewClickListener);
    }

    private void subscribeToViewModel() {
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

        viewModel.getReminderState().observe(getViewLifecycleOwner(), this::onChanged);

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

        viewModel.getInsertEmptyQuestWorkInfo().observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                navigateToEditSubQuestScreen(workInfo.getOutputData().getInt(com.elli0tt.rpg_life.presentation.worker.WorkerConstants.KEY_QUEST_ID, 0));
                viewModel.updateInsertEmptyQuestWorkRequest();
            }
        });

        viewModel.getName().observe(getViewLifecycleOwner(),
                name -> binding.nameEditText.clearFocus());
    }

    private void setupSubQuestsRecycler() {
        subQuestsAdapter = new SubQuestsAdapter();
        subQuestsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) ->
                viewModel.completeSubQuest(position, isCompleted));
        subQuestsAdapter.setOnRemoveButtonClickListener(position ->
                viewModel.removeSubQuest(position));
        subQuestsAdapter.setOnItemClickListener(position ->
                navigateToEditSubQuestScreen(viewModel.getSubQuestId(position)));
        subQuestsAdapter.setViewModel(viewModel);

        binding.subquestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        binding.subquestsRecycler.setAdapter(subQuestsAdapter);
    }

    @SuppressLint("MissingPermission")
    private void addToGoogleCalendar() {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        contentResolver.insert(CalendarContract.Events.CONTENT_URI,
                viewModel.getQuestContentValues());

        Snackbar.make(binding.getRoot(), R.string.add_edit_quest_added_to_google_calendar,
                Snackbar.LENGTH_SHORT).show();
    }

    private void setReminderAlarm() {
        Intent intent = new Intent(requireContext(), QuestReminderBroadcastReceiver.class);
        intent.putExtra(EXTRA_REMINDER_TITLE, viewModel.getName().getValue());
        intent.putExtra(EXTRA_NOTIFICATION_ID, viewModel.getQuestId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(),
                viewModel.getQuestId(), intent, 0);

        AlarmManager alarmManager =
                (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, viewModel.getReminderTime(), pendingIntent);
        }
    }

    private void showAddStartDatePopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.add_edit_quest_add_start_date_popup_menu,
                popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.add_edit_quest_add_start_date_popup_today) {
                viewModel.setStartDateToday();
            } else if (itemId == R.id.add_edit_quest_add_start_date_popup_tomorrow) {
                viewModel.setStartDateTomorrow();
            } else if (itemId == R.id.add_edit_quest_add_start_date_popup_next_week) {
                viewModel.setStartDateNextWeek();
            } else if (itemId == R.id.add_edit_quest_add_start_date_popup_pick_date) {
                pickDate(onStartDateSetListener);
            } else {
                return false;
            }
            return true;
        });
        popupMenu.show();
    }

    private void pickDate(DatePickerDialog.OnDateSetListener onDateSetListener) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                onDateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void pickTime(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener,
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
            int itemId = item.getItemId();
            if (itemId == R.id.add_edit_quest_add_date_due_popup_today) {
                viewModel.setDateDueToday();
            } else if (itemId == R.id.add_edit_quest_add_date_due_popup_tomorrow) {
                viewModel.setDateDueTomorrow();
            } else if (itemId == R.id.add_edit_quest_add_date_due_popup_next_week) {
                viewModel.setDateDueNextWeek();
            } else if (itemId == R.id.add_edit_quest_add_date_due_popup_pick_date) {
                pickDate(onDateDueSetListener);
            } else {
                return false;
            }

            return true;
        });
        popupMenu.show();
    }

    private void showRepeatPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.add_edit_quest_repeate_popup_menu,
                popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.add_edit_quest_repeat_popup_daily) {
                viewModel.setRepeatState(Quest.RepeatState.DAILY);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_weekdays) {
                viewModel.setRepeatState(Quest.RepeatState.WEEKDAYS);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_weekends) {
                viewModel.setRepeatState(Quest.RepeatState.WEEKENDS);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_weekly) {
                viewModel.setRepeatState(Quest.RepeatState.WEEKLY);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_monthly) {
                viewModel.setRepeatState(Quest.RepeatState.MONTHLY);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_yearly) {
                viewModel.setRepeatState(Quest.RepeatState.YEARLY);
            } else if (itemId == R.id.add_edit_quest_repeat_popup_custom) {
                //TODO create custom repeat popup action
            } else {
                return false;
            }

            return true;
        });
        popupMenu.show();
    }

    private void showDifficultyPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        Menu menu = popupMenu.getMenu();

        menu.add(Menu.NONE, DifficultyPopupMenuIds.VERY_EASY_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.VERY_EASY_POPUP_MENU_ITEM_ORDER, veryEasyTitle);
        menu.add(Menu.NONE, DifficultyPopupMenuIds.EASY_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.EASY_POPUP_MENU_ITEM_ORDER,
                easyTitle);
        menu.add(Menu.NONE, DifficultyPopupMenuIds.NORMAL_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.NORMAL_POPUP_MENU_ITEM_ORDER, normalTitle);
        menu.add(Menu.NONE, DifficultyPopupMenuIds.HARD_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.HARD_POPUP_MENU_ITEM_ORDER,
                hardTitle);
        menu.add(Menu.NONE, DifficultyPopupMenuIds.VERY_HARD_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.VERY_HARD_POPUP_MENU_ITEM_ORDER, veryHardTitle);
        menu.add(Menu.NONE, DifficultyPopupMenuIds.IMPOSSIBLE_POPUP_MENU_ITEM_ID,
                DifficultyPopupMenuIds.IMPOSSIBLE_POPUP_MENU_ITEM_ORDER, impossibleTitle);

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

    private void navigateToEditSubQuestScreen(int subQuestId) {
        AddEditQuestFragmentDirections.ActionAddEditQuestScreenSelf action =
                AddEditQuestFragmentDirections.actionAddEditQuestScreenSelf();
        action.setQuestId(subQuestId)
                .setParentQuestId(viewModel.getQuestId())
                .setIsSubQuest(true);
        navController.navigate(action);
    }

    private void navigateToAddSkillsToQuestScreen() {
        AddEditQuestFragmentDirections.ActionAddEditQuestScreenToAddSkillsToQuestFragment action =
                AddEditQuestFragmentDirections.actionAddEditQuestScreenToAddSkillsToQuestFragment();
        action.setQuestId(viewModel.getQuestId());
        navController.navigate(action);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                WRITE_CALENDAR) == PERMISSION_DENIED) {
            requestPermissions(new String[]{WRITE_CALENDAR},
                    REQUEST_PERMISSIONS);
        } else {
            addToGoogleCalendar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    addToGoogleCalendar();
                } else {
                    Snackbar.make(binding.getRoot(),
                            R.string.add_edit_quest_write_calendar_permission_denied,
                            Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void onChanged(Quest.ReminderState reminderState) {
        if (reminderState == Quest.ReminderState.NOT_SET) {
            binding.addReminderView.setText(R.string.add_edit_quest_remind_me);
            binding.addReminderView.setRemoveIconVisibility(View.INVISIBLE);
        } else if (reminderState == Quest.ReminderState.PICK_CUSTOM_DATE) {
            binding.addReminderView.setText(viewModel.getReminderDateFormatted());
            binding.addReminderView.setRemoveIconVisibility(View.VISIBLE);
        }
    }
}