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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestsFragment extends Fragment {

    private QuestsViewModel viewModel;

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

    private ActionMode actionMode;
    private ActionModeController actionModeController;

    private static final String SELECTION_ID = "quest fragment selection id";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quests, container, false);
        ButterKnife.bind(this, view);
        navController = NavHostFragment.findNavController(this);

        setupQuestsViewModel();
        setHasOptionsMenu(true);
        setupQuestsRecyclerView(view.findViewById(R.id.quests_list));

        return view;
    }

    @OnClick(R.id.quests_fab)
    void onFabClick(){
        navigateToAddQuestScreen();
    }

    private QuestsAdapter.OnItemClickListener onItemClickListener =
            position -> navigateToEditQuestScreen(allQuestsList.getValue().get(position).getId());

    private void setupQuestsRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(questsAdapter);
        questsAdapter.setOnItemClickListener(onItemClickListener);
//        questsAdapter.setOnItemLongClickListener(position -> {
//            if (actionMode != null) {
//                return;
//            }
//            actionMode = getActivity().startActionMode(actionModeCallback);
//        });
        questsAdapter.setOnIsCompleteCheckBoxClickListener((isCompleted, position) -> {
            Quest currentQuest = allQuestsList.getValue().get(position);
            currentQuest.setCompleted(isCompleted);
            viewModel.update(currentQuest);
        });
        questsAdapter.setOnIsImportantCheckBoxClickListener((isImportant, position) -> {
            Quest currentQuest = allQuestsList.getValue().get(position);
            currentQuest.setImportant(isImportant);
            viewModel.update(currentQuest);
        });

        SelectionTracker<Long> selectionTracker = new SelectionTracker.Builder<>(
                SELECTION_ID,
                recyclerView,
                new QuestsItemKeyProvider(recyclerView),
                new QuestsItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {


            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection() && actionMode == null) {
                    AppCompatActivity activity = (AppCompatActivity) getActivity();
                    if (activity != null) {
                        actionMode = activity.startSupportActionMode(
                                new ActionModeController(selectionTracker, viewModel));
                        questsAdapter.removeOnItemClickListener();
                    }
                } else if (!selectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                    questsAdapter.setOnItemClickListener(onItemClickListener);
                }
            }

        });

        questsAdapter.setSelectionTracker(selectionTracker);

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
        viewModel = ViewModelProviders.of(this).get(QuestsViewModel.class);
        allQuestsList = viewModel.getAllQuestsList();
        allQuestsList.observe(this, questList -> questsAdapter.submitList(questList));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quests_toolbar_menu, menu);
        deleteAllToolbarMenuItem = menu.findItem(R.id.quests_toolbar_menu_delete_all);
        populateWithSamplesMenuItem = menu.findItem(R.id.quests_toolbar_menu_populate_with_samples);
        deleteToolbarMenuItem = menu.findItem(R.id.quests_toolbar_menu_delete);
        selectAllMenuItem = menu.findItem(R.id.quests_toolbar_menu_select_all);
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
                viewModel.deleteAll();
                break;
            case R.id.quests_toolbar_menu_populate_with_samples:
                viewModel.populateWithSamples();
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
