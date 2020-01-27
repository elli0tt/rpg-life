package com.elli0tt.rpg_life.presentation.quests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.Quest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;



public class QuestsFragment extends Fragment {

    private QuestsViewModel viewModel;

    private QuestsAdapter questsAdapter = new QuestsAdapter();
    private NavController navController;
    //private LiveData<List<Quest>> allQuestsList;

    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private ActionMode actionMode;

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
        recyclerView = view.findViewById(R.id.quests_list);

        setupQuestsViewModel();
        setHasOptionsMenu(true);
        setupQuestsRecyclerView(view.findViewById(R.id.quests_list));

        fab.setOnClickListener(v -> navigateToAddQuestScreen());
    }

    private QuestsAdapter.OnItemClickListener onItemClickListener = position -> {
        if (viewModel.getQuests().getValue() != null) {
            navigateToEditQuestScreen(viewModel.getQuests().getValue().get(position).getId());
        }
    };

    private void setupQuestsRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(questsAdapter);
        questsAdapter.setOnItemClickListener(onItemClickListener);
        questsAdapter.setOnItemLongClickListener(position -> {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                actionMode = activity.startSupportActionMode(
                        new ActionModeController(viewModel, questsAdapter));
            }
            questsAdapter.startSelection(position);
        });
        questsAdapter.setOnSelectionFinishedListener(() -> {
            if (actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        });
        questsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) -> {
            if (viewModel.getQuests().getValue() != null) {
                Quest currentQuest = viewModel.getQuests().getValue().get(position);
                currentQuest.setCompleted(isCompleted);
                viewModel.update(currentQuest);
            }
        });
        questsAdapter.setOnIsImportantCheckBoxClickListener((isImportant, position) -> {
            if (viewModel.getQuests().getValue() != null) {
                Quest currentQuest = viewModel.getQuests().getValue().get(position);
                currentQuest.setImportant(isImportant);
                viewModel.update(currentQuest);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL
                , false));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                Objects.requireNonNull(getContext()), RecyclerView.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fab.isShown()) {
                    fab.show();
                } else if (dy > 0 && fab.isShown()) {
                    fab.hide();
                }
            }
        });
    }

    private void setupQuestsViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
        viewModel.getQuests().observe(this, questList -> questsAdapter.submitList(questList));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quests_toolbar_menu_filter:
                showFilteringPopUpMenu(getActivity().findViewById(R.id.quests_toolbar_menu_filter));
                break;
            case R.id.quests_toolbar_menu_delete_all:
                viewModel.deleteAll();
                break;
            case R.id.quests_toolbar_menu_populate_with_samples:
                viewModel.populateWithSamples();
                break;

        }
        return true;
    }

    private void showFilteringPopUpMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.quests_filtering_pop_up_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.quests_filtering_popup_menu_all:
                    viewModel.setFiltering(QuestsFilterType.ALL);
                    return true;
                case R.id.quests_filtering_popup_menu_active:
                    viewModel.setFiltering(QuestsFilterType.ACTIVE);
                    return true;
                case R.id.quests_filtering_popup_menu_completed:
                    viewModel.setFiltering(QuestsFilterType.COMPLETED);
                    return true;
                default:
                    return false;

            }

        });

        popupMenu.show();
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
