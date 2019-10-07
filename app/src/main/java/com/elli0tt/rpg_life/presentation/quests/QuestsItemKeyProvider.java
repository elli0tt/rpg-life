package com.elli0tt.rpg_life.presentation.quests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.elli0tt.rpg_life.domain.modal.Quest;

import java.util.List;

public class QuestsItemKeyProvider extends ItemKeyProvider<Long> {

//    private final List<Quest> itemsList;
//
//    /**
//     * Creates a new provider with the given scope.
//     *
//     * @param scope Scope can't be changed at runtime.
//     */
//    public QuestsItemKeyProvider(int scope, List<Quest> itemsList) {
//        super(scope);
//        this.itemsList = itemsList;
//    }
//
//    @Nullable
//    @Override
//    public Long getKey(int position) {
//        return (long)itemsList.get(position).getId();
//    }
//
//    @Override
//    public int getPosition(@NonNull Long key) {
//        for (int i = 0; i < itemsList.size(); ++i){
//            if (itemsList.get(i).getId() == key){
//                return i;
//            }
//        }
//        //This will never happen
//        //Id should be found
//        return 0;
//    }

    private RecyclerView recyclerView;

    public QuestsItemKeyProvider(RecyclerView recyclerView) {
        super(SCOPE_MAPPED);
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return recyclerView.getAdapter().getItemId(position);
    }

    @Override
    public int getPosition(@NonNull Long key) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForItemId(key);
        return viewHolder != null ? viewHolder.getLayoutPosition() : RecyclerView.NO_POSITION;
    }
}
