package com.elli0tt.rpg_life.presentation.quests;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class QuestsItemDetailsLookup extends androidx.recyclerview.selection.ItemDetailsLookup<Long> {
    private RecyclerView recyclerView;

    public QuestsItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            return ((QuestsAdapter.QuestsViewHolder) recyclerView.getChildViewHolder(view)).getItemDetails();
        }
        return null;
    }
}
