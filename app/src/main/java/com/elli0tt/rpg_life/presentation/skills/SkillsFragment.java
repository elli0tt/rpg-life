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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SkillsFragment extends Fragment {
    private SkillsViewModel viewModel;

    private RecyclerView recyclerView;
    private FloatingActionButton addSkillFab;

    private NavController navController;

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

        setupRecyclerView();
        setHasOptionsMenu(true);

        addSkillFab.setOnClickListener(onAddSkillFabClickListener);
    }

    private View.OnClickListener onAddSkillFabClickListener = v -> navigateToAddSkillScreen();

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        SkillsAdapter adapter = new SkillsAdapter();
        viewModel.getSkillsToShow().observe(getViewLifecycleOwner(), adapter::submitList);
        adapter.setOnStartTimerFabClickListener(this::navigateToCountDownScreen);
        recyclerView.setAdapter(adapter);
        if (getContext() != null) {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        }
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
                viewModel.setSortingState(SkillsSortingState.NAME);
                return true;
            case R.id.sort_by_level:
                viewModel.setSortingState(SkillsSortingState.LEVEL);
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
