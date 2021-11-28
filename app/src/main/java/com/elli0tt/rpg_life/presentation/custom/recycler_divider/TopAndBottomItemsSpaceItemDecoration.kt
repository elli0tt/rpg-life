package com.elli0tt.rpg_life.presentation.custom.recycler_divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TopAndBottomItemsSpaceItemDecoration(
    private val topOffset: Int,
    private val bottomOffset: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topOffset
        }
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.bottom = bottomOffset
        }
    }

}