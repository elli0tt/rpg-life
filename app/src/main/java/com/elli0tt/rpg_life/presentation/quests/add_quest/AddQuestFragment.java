package com.elli0tt.rpg_life.presentation.quests.add_quest;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;
import com.elli0tt.rpg_life.presentation.quests.BaseAddEditQuestFragment;

public class AddQuestFragment extends BaseAddEditQuestFragment {
    private AddQuestViewModel addQuestViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addQuestViewModel = ViewModelProviders.of(getActivity()).get(AddQuestViewModel.class);
    }

    private void saveQuest() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        @Quest.Difficulty int difficulty = difficultySpinner.getSelectedItemPosition();
        addQuestViewModel.saveQuest(name, description, difficulty);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_quest_confirm_button:
                saveQuest();
                navController.popBackStack();
                break;
        }
        return true;
    }




}
