package com.elli0tt.rpg_life.presentation.screen.skills;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.presentation.custom_view.UpDownArrowsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkillsFragment extends Fragment {
    private SkillsViewModel viewModel;

    private RecyclerView recyclerView;
    private FloatingActionButton addSkillFab;
    private UpDownArrowsView sortUpDownArrowsView;

    private NavController navController;
    private SkillsAdapter skillsAdapter = new SkillsAdapter();
    private View.OnClickListener onAddSkillFabClickListener = v -> viewModel.insertEmptySkill();
    private View.OnClickListener onSortUpDownArrowsViewClickListener =
            view -> viewModel.changeSortingDirection();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SkillsViewModel.class);
        navController = NavHostFragment.findNavController(this);

        recyclerView = view.findViewById(R.id.skills_recycler_view);
        addSkillFab = view.findViewById(R.id.add_skill_fab);
        sortUpDownArrowsView = view.findViewById(R.id.sort_up_down_arrows_view);

        setupRecyclerView();
        subscribeToViewModel();
        setHasOptionsMenu(true);

        addSkillFab.setOnClickListener(onAddSkillFabClickListener);
        sortUpDownArrowsView.setOnViewClickListener(onSortUpDownArrowsViewClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                navigateToEditSkillScreen(workInfo.getOutputData().getInt(Constants.KEY_SKILL_ID,
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
        switch (item.getItemId()) {
            case R.id.delete_all:
                viewModel.deleteAll();
                return true;
            case R.id.populate_with_samples:
                viewModel.populateWithSamples();
                return true;
            case R.id.sort_by_name:
                viewModel.setSortingState(SkillsSortingState.NAME_ASC);
                return true;
            case R.id.sort_by_level:
                viewModel.setSortingState(SkillsSortingState.LEVEL_ASC);
                return true;
            default:
                return false;
        }
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
