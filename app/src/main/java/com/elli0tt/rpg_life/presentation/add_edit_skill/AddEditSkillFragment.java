package com.elli0tt.rpg_life.presentation.add_edit_skill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.databinding.FragmentAddEditSkillBinding;
import com.elli0tt.rpg_life.presentation.utils.SoftKeyboardUtil;

public class AddEditSkillFragment extends Fragment {
    private AddEditSkillViewModel viewModel;

    private NavController navController;

    private EditText nameEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddEditSkillViewModel.class);
        FragmentAddEditSkillBinding binding = FragmentAddEditSkillBinding.inflate(inflater,
                container, false);

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        nameEditText = view.findViewById(R.id.add_edit_skill_name_edit_text);

        nameEditText.setOnFocusChangeListener(onEditTextsFocusChangeListener);

        subscribeToViewModel();
        setHasOptionsMenu(true);
    }

    private View.OnFocusChangeListener onEditTextsFocusChangeListener = (v, hasFocus) -> {
        if (!hasFocus) {
            SoftKeyboardUtil.hideKeyboard(v, getActivity());
        }
    };

    private void subscribeToViewModel() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_edit_skill_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_edit_skill_menu_save:
                viewModel.saveSkill();
                navController.popBackStack();
                return true;
        }
        return false;
    }
}
