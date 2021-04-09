package com.elli0tt.rpg_life.presentation.screen.quests;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.presentation.adapter.quests.QuestsAdapter;

public class ActionModeController implements ActionMode.Callback {

    private QuestsViewModel viewModel;
    private QuestsAdapter adapter;

    ActionModeController(QuestsViewModel viewModel, QuestsAdapter adapter) {
        this.viewModel = viewModel;
        this.adapter = adapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.quests_selection_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.quest_selection_toolbar_menu_select_all) {
            selectAll();
            return true;
        } else if (itemId == R.id.quest_selection_toolbar_menu_delete) {
            deleteSelected();
            return true;
        }
        return false;
    }

    private void selectAll() {
        adapter.selectAll();
    }

    private void deleteSelected() {
        viewModel.delete(adapter.getSelectedQuests());
        viewModel.finishSelection();
        adapter.finishSelection();
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        viewModel.finishSelection();
        adapter.finishSelection();
    }

}
