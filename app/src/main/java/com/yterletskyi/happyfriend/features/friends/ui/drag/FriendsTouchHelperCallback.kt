package com.yterletskyi.happyfriend.features.friends.ui.drag

import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.yterletskyi.happyfriend.common.x.dp
import com.yterletskyi.happyfriend.features.friends.ui.FriendsAdapterDelegate

class FriendsTouchHelperCallback(
    private val onFriendMoved: (from: Int, to: Int) -> Unit,
    private val onDragEnded: () -> Unit,
    private val onFriendSwiped: (index: Int) -> Unit,
) : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT) {

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
        onFriendMoved(fromPos, toPos)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = viewHolder.itemViewType == FriendsAdapterDelegate.FRIEND_ITEM_VIEW_TYPE

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onFriendSwiped(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.let {
                ViewCompat.setElevation(it, DRAG_HIGHLIGHT_ELEVATION)
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.let {
            ViewCompat.setElevation(it, 0f)
        }
        onDragEnded()
    }

    companion object {
        private val DRAG_HIGHLIGHT_ELEVATION = 8f.dp
    }
}