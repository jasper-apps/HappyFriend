package com.yterletskyi.happyfriend.features.friends.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper

class FriendsTouchHelper(
    onFriendMoved: (from: Int, to: Int) -> Unit,
    onFriendSwiped: (index: Int) -> Unit,
    onSwipeEnded: () -> Unit = {},
    onDragEnded: () -> Unit = {},
) : ItemTouchHelper(
    FriendsTouchHelperCallback(onFriendMoved, onFriendSwiped, onSwipeEnded, onDragEnded)
)
