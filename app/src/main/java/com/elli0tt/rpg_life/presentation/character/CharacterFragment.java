package com.elli0tt.rpg_life.presentation.character;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.presentation.custom_view.DividerItemDecoration;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CharacterFragment extends Fragment {

    private NavController navController;
    private CharacteristicsAdapter adapter;
    private CharacteristicsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView(view.findViewById(R.id.characteristics_list));
        navController = NavHostFragment.findNavController(this);

        setupCharacteristicsViewModel();
        setHasOptionsMenu(true);
        return view;
    }

    @OnClick(R.id.character_fab)
    void onFabClick() {
        navigateToAddCharacteristicFragment();
    }

    public void navigateToAddCharacteristicFragment() {
        navController.navigate(R.id.action_character_screen_to_add_characteristic_fragment);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CharacteristicsAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void setupCharacteristicsViewModel() {
        viewModel = ViewModelProviders.of(this)
                .get(CharacteristicsViewModel.class);
        viewModel.getAllCharacteristics().observe(this,
                characteristics -> adapter.setCharacteristicList(characteristics));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.character_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.character_toolbar_item_populate_with_samples:
                viewModel.populateWithSamples();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
