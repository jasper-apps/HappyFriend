package com.yterletskyi.happyfriend.features.ideas.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView

class MoveIdeaTouchHelperCallback(
    private val onIdeaMoved: (from: Int, to: Int) -> Unit,
    private val onDragEnded: () -> Unit,
) : ItemTouchHelper.SimpleCallback(UP or DOWN, 0) {

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        recyclerView.adapter?.notifyItemMoved(fromPos, toPos)
        onIdeaMoved(fromPos, toPos)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.itemViewType == 1 && target.itemViewType == 1
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        // TODO: highlight current item
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // TODO: clear highlighting
        onDragEnded()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
        throw IllegalStateException("Swipes are not allowed")
}