package com.elli0tt.rpg_life.presentation.quests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestsFragment extends Fragment {

    private QuestsViewModel questsViewModel;

    private QuestsAdapter questsAdapter = new QuestsAdapter();
    private NavController navController;
    private LiveData<List<Quest>> allQuestsList;

    private MenuItem deleteAllToolbarMenuItem;
    private MenuItem populateWithSamplesMenuItem;
    private MenuItem selectAllMenuItem;
    private MenuItem deleteToolbarMenuItem;

    @BindView(R.id.quests_fab) FloatingActionButton fab;

    private enum Mode {
        NORMAL, REMOVE
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests, container, false);
        ButterKnife.bind(this, view);
        navController = NavHostFragment.findNavController(this);

        setHasOptionsMenu(true);
        setupQuestsRecyclerView(view.findViewById(R.id.quests_list));
        setupQuestsViewModel();
        return view;
    }

    @OnClick(R.id.quests_fab)
    void onFabClick(){
        navigateToAddQuestScreen();
    }

    private void setupQuestsRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(questsAdapter);
        questsAdapter.setOnItemClickListener(position -> {
            navigateToEditQuestScreen(allQuestsList.getValue().get(position).getId());
        });
        questsAdapter.setOnItemLongClickListener(position -> startMode(Mode.REMOVE));
        questsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) -> {
            Quest currentQuest = allQuestsList.getValue().get(position);
            currentQuest.setCompleted(isCompleted);
            questsViewModel.update(currentQuest);
        });
        questsAdapter.setOnIsImportantCheckBoxClickListener((isImportant, position) -> {
            Quest currentQuest = allQuestsList.getValue().get(position);
            currentQuest.setImportant(isImportant);
            questsViewModel.update(currentQuest);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), RecyclerView.VERTICAL));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
           @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
               if (dy < 0 && !fab.isShown())
                   fab.show();
               else if(dy > 0 && fab.isShown())
                   fab.hide();
            }
        });
    }

    private void setupQuestsViewModel() {
        questsViewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
        allQuestsList = questsViewModel.getAllQuestsList();
        allQuestsList.observe(this, questList -> questsAdapter.submitList(questList));
    }

    private List<Quest> generateSampleQuestsList() {
        List<Quest> result = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            result.add(new Quest("Quest " + i));
        }
        return result;
    }

    private void populateDataBaseWithSamples() {
        questsViewModel.insert(generateSampleQuestsList());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu);
        deleteAllToolbarMenuItem = menu.findItem(R.id.quests_toolbar_menu_delete_all);
        populateWithSamplesMenuItem = menu.findItem(R.id.quest_toolbar_menu_populate_with_samples);
        deleteToolbarMenuItem = menu.findItem(R.id.quest_toolbar_menu_delete);
        selectAllMenuItem = menu.findItem(R.id.quest_toolbar_menu_select_all);
        startMode(Mode.NORMAL);
    }

    private void startMode(Mode modeToStart) {
        switch (modeToStart) {
            case NORMAL:
                deleteAllToolbarMenuItem.setVisible(true);
                populateWithSamplesMenuItem.setVisible(true);
                deleteToolbarMenuItem.setVisible(false);
                selectAllMenuItem.setVisible(false);
                break;
            case REMOVE:
                deleteAllToolbarMenuItem.setVisible(false);
                populateWithSamplesMenuItem.setVisible(false);
                deleteToolbarMenuItem.setVisible(true);
                selectAllMenuItem.setVisible(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quests_toolbar_menu_delete_all:
                questsViewModel.deleteAll();
                break;
            case R.id.quest_toolbar_menu_populate_with_samples:
                populateDataBaseWithSamples();
                break;

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
