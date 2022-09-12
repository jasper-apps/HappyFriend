package com.yterletskyi.happyfriend.features.friends.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.yterletskyi.happyfriend.common.list.RecyclerDelegationAdapter
import com.yterletskyi.happyfriend.features.friends.data.GlobalFriends
import com.yterletskyi.happyfriend.features.friends.domain.FriendModelItem
import com.yterletskyi.happyfriend.features.friends.ui.FriendsAdapterDelegate

class FriendsTouchHelperCallback(
    private val onFriendMoved: (from: Int, to: Int) -> Unit,
    private val onFriendSwiped: (index: Int) -> Unit,
    private val onSwipeEnded: () -> Unit,
    private val onDragEnded: () -> Unit,
) : ItemTouchHelper.SimpleCallback(0, 0) {

    private var lastActionState: Int = ItemTouchHelper.ACTION_STATE_DRAG

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

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val adapter = recyclerView.adapter as? RecyclerDelegationAdapter
            ?: throw IllegalStateException(
                "Adapter cannot be cast to RecyclerDelegationAdapter or not set"
            )

        val model = adapter.getItem(viewHolder.adapterPosition) as? FriendModelItem
            ?: throw IllegalStateException(
                "Only FriendModelItem view type is supported by this class"
            )

        val dragFlags = UP or DOWN
        val swipeFlats = when (model.id) {
            GlobalFriends.MyWishlistFriend.id,
            GlobalFriends.GeneralIdeas.id -> 0
            else -> START
        }

        return makeMovementFlags(dragFlags, swipeFlats)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        lastActionState = ItemTouchHelper.ACTION_STATE_DRAG
        return viewHolder.itemViewType == FriendsAdapterDelegate.FRIEND_ITEM_VIEW_TYPE
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        lastActionState = ItemTouchHelper.ACTION_STATE_SWIPE
        onFriendSwiped(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        when (lastActionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> onDragEnded()
            ItemTouchHelper.ACTION_STATE_SWIPE -> onSwipeEnded()
        }
        lastActionState = ItemTouchHelper.ACTION_STATE_IDLE
    }
}
