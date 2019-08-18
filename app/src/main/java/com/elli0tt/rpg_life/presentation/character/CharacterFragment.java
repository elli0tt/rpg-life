package com.elli0tt.rpg_life.presentation.character;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Characteristic;
import com.elli0tt.rpg_life.presentation.custom_view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CharacterFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private NavController navController;
    private CharacteristicsAdapter adapter;
    private CharacteristicsViewModel characteristicsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView(view.findViewById(R.id.characteristics_list));
        navController = NavHostFragment.findNavController(this);
        view.findViewById(R.id.button).setOnClickListener(v -> showPopupMenu(v));

        setupCharacteristicsViewModel();

        return view;
    }

    @OnClick(R.id.character_fab)
    void onFabClick() {
        navigateToAddCharacteristicFragment();
    }

    public void navigateToAddCharacteristicFragment() {
        navController.navigate(R.id.action_character_screen_to_add_characteristic_fragment);
    }

    private void populateDatabaseWithSampleList() {
        characteristicsViewModel.insert(generateSampleCharacteristicList());
    }

    //For test only
    //TODO Don't forget to delete
    private List<Characteristic> generateSampleCharacteristicList() {
        List<Characteristic> resultList = new ArrayList<>();
        resultList.add(new Characteristic("Strength"));
        resultList.add(new Characteristic("Intelligence"));
        resultList.add(new Characteristic("Agility"));
        resultList.add(new Characteristic("Endurance"));
        resultList.add(new Characteristic("Willpower"));
        resultList.add(new Characteristic("Procrastination"));
        resultList.add(new Characteristic("Self-confidence"));
        resultList.add(new Characteristic("Communication"));
        return resultList;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CharacteristicsAdapter();
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()));
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void showPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(getContext(), view);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.characteristic_item_menu);
        menu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void setupCharacteristicsViewModel() {
        characteristicsViewModel = ViewModelProviders.of(this)
                .get(CharacteristicsViewModel.class);
        characteristicsViewModel.getAllCharacteristics().observe(this, new Observer<List<Characteristic>>() {
            @Override
            public void onChanged(List<Characteristic> characteristics) {
                adapter.setCharacteristicList(characteristics);
            }
        });
    }
}
