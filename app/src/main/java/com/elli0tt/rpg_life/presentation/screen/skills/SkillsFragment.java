package com.elli0tt.rpg_life.presentation.screen.skills;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.model.SkillsSortingState;
import com.elli0tt.rpg_life.presentation.adapter.skills.SkillsAdapter;
import com.elli0tt.rpg_life.presentation.core.fragment.BaseFragment;
import com.elli0tt.rpg_life.presentation.custom.view.UpDownArrowsView;
import com.elli0tt.rpg_life.presentation.screen.skills.di.SkillsComponent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

public class SkillsFragment extends BaseFragment {

    private final SkillsAdapter skillsAdapter = new SkillsAdapter();
    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private SkillsComponent skillsComponent;
    private SkillsViewModel viewModel;

    private final View.OnClickListener onAddSkillFabClickListener =
            v -> viewModel.insertEmptySkill();
    private final View.OnClickListener onSortUpDownArrowsViewClickListener =
            view -> viewModel.changeSortingDirection();

    private RecyclerView recyclerView;
    private FloatingActionButton addSkillFab;
    private UpDownArrowsView sortUpDownArrowsView;
    private NavController navController;

    public SkillsFragment() {
        super(R.layout.fragment_skills);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDagger();

        navController = NavHostFragment.findNavController(this);

        recyclerView = view.findViewById(R.id.skills_recycler_view);
        addSkillFab = view.findViewById(R.id.add_skill_fab);
        sortUpDownArrowsView = view.findViewById(R.id.sort_up_down_arrows_view);

        setupToolbar();
        setupRecyclerView();
        subscribeToViewModel();
        setHasOptionsMenu(true);

        addSkillFab.setOnClickListener(onAddSkillFabClickListener);
        sortUpDownArrowsView.setOnViewClickListener(onSortUpDownArrowsViewClickListener);
    }

    private void initDagger() {
        skillsComponent = getAppComponent().skillsComponentFactory().create();
        skillsComponent.inject(this);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(SkillsViewModel.class);
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar supportActionBar = activity.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        skillsAdapter.setOnStartTimerFabClickListener(this::navigateToCountDownScreen);
        skillsAdapter.setOnItemClickListener(position ->
                navigateToEditSkillScreen(viewModel.getSkillId(position)));
        recyclerView.setAdapter(skillsAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && !addSkillFab.isShown()) {
                    addSkillFab.show();
                } else if (dy > 0 && addSkillFab.isShown()) {
                    addSkillFab.hide();
                }
            }
        });
    }

    private void subscribeToViewModel() {
        viewModel.getSkillsToShow().observe(getViewLifecycleOwner(), skillsAdapter::submitList);

        viewModel.getSortedByTextResId().observe(getViewLifecycleOwner(),
                textResId -> sortUpDownArrowsView.setText(textResId));

        viewModel.getSortingState().observe(getViewLifecycleOwner(), skillsSortingState -> {
            switch (skillsSortingState) {
                case NAME_ASC:
                case LEVEL_ASC:
                    sortUpDownArrowsView.setArrowUp();
                    break;
                case NAME_DESC:
                case LEVEL_DESC:
                    sortUpDownArrowsView.setArrowDown();
                    break;
            }
        });

        viewModel.getInsertEmptySkillWorkInfo().observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo != null && workInfo.getState().isFinished()) {
                navigateToEditSkillScreen(workInfo.getOutputData().getInt(SkillsConstants.KEY_SKILL_ID,
                        0));
                viewModel.updateInsertEmptySkillWorkRequest();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.skills_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.delete_all) {
            viewModel.deleteAll();
        } else if (itemId == R.id.populate_with_samples) {
            viewModel.populateWithSamples();
        } else if (itemId == R.id.sort_by_name) {
            viewModel.setSortingState(SkillsSortingState.NAME_ASC);
        } else if (itemId == R.id.sort_by_level) {
            viewModel.setSortingState(SkillsSortingState.LEVEL_ASC);
        } else {
            return false;
        }
        return true;
    }

    private void navigateToCountDownScreen() {
        navController.navigate(R.id.countdown_timer_screen);
    }

    private void navigateToAddSkillScreen() {
        SkillsFragmentDirections.ActionSkillsScreenToAddSkillScreen action =
                SkillsFragmentDirections.actionSkillsScreenToAddSkillScreen();
        navController.navigate(action);
    }

    private void navigateToEditSkillScreen(int skillId) {
        SkillsFragmentDirections.ActionSkillsScreenToEditSkillScreen action =
                SkillsFragmentDirections.actionSkillsScreenToEditSkillScreen();
        action.setSkillId(skillId);
        navController.navigate(action);
    }
}
