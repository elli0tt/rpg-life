package com.elli0tt.rpg_life.presentation.quests.edit_quest;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;
import com.elli0tt.rpg_life.presentation.quests.BaseAddEditQuestFragment;

public class EditQuestFragment extends BaseAddEditQuestFragment {

    private EditQuestViewModel editQuestViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editQuestViewModel = ViewModelProviders.of(getActivity()).get(EditQuestViewModel.class);
        setupViewsStartValues();
    }

    private void setupViewsStartValues() {
        Quest currentQuest = editQuestViewModel.getCurrentQuest().getValue();
        nameEditText.setText(currentQuest.getName());
        descriptionEditText.setText(currentQuest.getDescription());
        difficultySpinner.setSelection(currentQuest.getDifficulty());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_quest_confirm_button:
                editQuest();
                navController.popBackStack();
                break;
        }
        return true;
    }

    private void editQuest(){
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        @Quest.Difficulty int difficulty = difficultySpinner.getSelectedItemPosition();
        editQuestViewModel.editQuest(name, description, difficulty);
    }
}
