package com.elli0tt.rpg_life.presentation.quests;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.selection.SelectionTracker;

import com.elli0tt.rpg_life.R;
import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActionModeController implements ActionMode.Callback {

    private SelectionTracker<Long> tracker;
    private List<Quest> allItems;
    private QuestsViewModel viewModel;

    public ActionModeController(SelectionTracker<Long> tracker, QuestsViewModel viewModel) {
        this.tracker = tracker;
        this.viewModel = viewModel;
    }

    public void setAllItems(List<Quest> allItems){
        this.allItems = allItems;
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
        switch (item.getItemId()) {
            case R.id.quest_selection_toolbar_menu_select_all:
                selectAll();
                return true;

            case R.id.quest_selection_toolbar_menu_delete:

                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        tracker.clearSelection();
    }

    private void selectAll() {
        tracker.setItemsSelected(viewModel.getAllKeys(), true);
    }
}
