package com.elli0tt.rpg_life.presentation.quests;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class QuestsFragment extends Fragment {

    private QuestsViewModel viewModel;

    private QuestsAdapter questsAdapter = new QuestsAdapter();
    private NavController navController;

    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private ActionMode actionMode;

    private boolean isToShowDevelopersOptions;

    private MenuItem showCompletedMenuItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        fab = view.findViewById(R.id.quests_fab);
        recyclerView = view.findViewById(R.id.quests_recycler_view);

        subscribeToViewModel();
        setHasOptionsMenu(true);
        setupQuestsRecyclerView();

        fab.setOnClickListener(v -> navigateToAddQuestScreen());
    }

    @Override
    public void onResume() {
        super.onResume();
        questsAdapter.notifyDataSetChanged();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        isToShowDevelopersOptions = sharedPreferences.getBoolean("developers_options", false);
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    private QuestsAdapter.OnItemClickListener onItemClickListener = position -> {
        if (viewModel.getQuests().getValue() != null) {
            navigateToEditQuestScreen(viewModel.getQuests().getValue().get(position).getId());
        }
    };

    private void setupQuestsRecyclerView() {
        recyclerView.setAdapter(questsAdapter);
        questsAdapter.setOnItemClickListener(onItemClickListener);
        questsAdapter.setOnItemLongClickListener(position -> {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                actionMode = activity.startSupportActionMode(
                        new ActionModeController(viewModel, questsAdapter));
            }
            viewModel.startSelection();
            questsAdapter.startSelection(position);
        });
        questsAdapter.setOnSelectionFinishedListener(() -> {
            if (actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        });
        questsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) ->
                viewModel.completeQuest(position, isCompleted));
        questsAdapter.setOnIsImportantCheckBoxClickListener((isImportant, position) ->
                viewModel.setQuestImportant(position, isImportant));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL
                , false));
//        recyclerView.addItemDecoration(new DividerItemDecoration(
//                Objects.requireNonNull(getContext()), RecyclerView.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown() && !viewModel.isSelectionStarted().getValue()) {
                    fab.show();
                } else if (dy > 0 && fab.isShown()) {
                    fab.hide();
                }
            }
        });
    }

    private void subscribeToViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
        viewModel.getQuests().observe(getViewLifecycleOwner(), questList -> {
            questsAdapter.submitList(questList);
        });
        viewModel.isSelectionStarted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSelectionStarted) {
                if (isSelectionStarted) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        viewModel.getShowCompletedTextResId().observe(getViewLifecycleOwner(),
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer textResId) {
                        if (showCompletedMenuItem != null) {
                            showCompletedMenuItem.setTitle(textResId);
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu);

        MenuItem populateWithSamplesItem =
                menu.findItem(R.id.quests_toolbar_menu_populate_with_samples);
        MenuItem deleteAllItem = menu.findItem(R.id.quests_toolbar_menu_delete_all);
        showCompletedMenuItem = menu.findItem(R.id.quests_toolbar_menu_show_completed);

        populateWithSamplesItem.setVisible(isToShowDevelopersOptions);
        deleteAllItem.setVisible(isToShowDevelopersOptions);
        showCompletedMenuItem.setTitle(viewModel.getShowCompletedTextResId().getValue());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quests_toolbar_menu_delete_all:
                viewModel.deleteAll();
                fab.show();
                break;
            case R.id.quests_toolbar_menu_populate_with_samples:
                viewModel.populateWithSamples();
                break;
            case R.id.quests_toolbar_menu_sort_by_name:
                viewModel.setSorting(QuestsSortingState.NAME);
                break;
            case R.id.quests_toolbar_menu_sort_by_date_due:
                viewModel.setSorting(QuestsSortingState.DATE_DUE);
                break;
            case R.id.quests_toolbar_menu_sort_by_date_added:
                viewModel.setSorting(QuestsSortingState.DATE_ADDED);
                break;
            case R.id.quests_toolbar_menu_sort_by_difficulty:
                viewModel.setSorting(QuestsSortingState.DIFFICULTY);
                break;
            case R.id.quests_toolbar_menu_filtering_all:
                viewModel.setFiltering(QuestsFilterState.ALL);
                break;
            case R.id.quests_toolbar_menu_filtering_important:
                viewModel.setFiltering(QuestsFilterState.IMPORTANT);
                break;
            case R.id.quests_toolbar_menu_filter_today:
                viewModel.setFiltering(QuestsFilterState.TODAY);
                break;
            case R.id.quests_toolbar_menu_filter_tomorrow:
                viewModel.setFiltering(QuestsFilterState.TOMORROW);
                break;
            case R.id.quests_toolbar_menu_show_completed:
                viewModel.changeShowCompleted();
        }
        return true;
    }

    private void navigateToAddQuestScreen() {
        QuestsFragmentDirections.ActionQuestsScreenToAddQuestScreen action =
                QuestsFragmentDirections.actionQuestsScreenToAddQuestScreen();
        navController.navigate(action);
    }

    private void navigateToEditQuestScreen(int questId) {
        QuestsFragmentDirections.ActionQuestsScreenToEditQuestScreen action =
                QuestsFragmentDirections.actionQuestsScreenToEditQuestScreen();
        action.setQuestId(questId);
        navController.navigate(action);
    }
}
