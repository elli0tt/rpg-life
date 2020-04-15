package com.elli0tt.rpg_life.presentation.skills;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.presentation.custom_view.DividerItemDecoration;
import com.elli0tt.rpg_life.presentation.custom_view.UpDownArrowsView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkillsFragment extends Fragment {
    private SkillsViewModel viewModel;

    private RecyclerView recyclerView;
    private FloatingActionButton addSkillFab;
    private UpDownArrowsView sortUpDownArrowsView;

    private NavController navController;
    private SkillsAdapter skillsAdapter = new SkillsAdapter();

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

    private View.OnClickListener onAddSkillFabClickListener = v -> navigateToAddSkillScreen();

    private View.OnClickListener onSortUpDownArrowsViewClickListener = view -> {
        viewModel.changeSortingDirection();
    };

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        skillsAdapter.setOnStartTimerFabClickListener(this::navigateToCountDownScreen);
        recyclerView.setAdapter(skillsAdapter);
        if (getContext() != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        }
    }

    private void subscribeToViewModel() {
        viewModel.getSkillsToShow().observe(getViewLifecycleOwner(), skillsAdapter::submitList);

        viewModel.getSortedByTextResId().observe(getViewLifecycleOwner(), textResId -> {
            sortUpDownArrowsView.setText(textResId);
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
        NavDirections action = SkillsFragmentDirections.actionSkillsScreenToAddSkillScreen();
        navController.navigate(action);
    }
}
