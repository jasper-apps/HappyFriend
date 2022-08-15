package com.yterletskyi.happyfriend.features.friends.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper

class FriendsTouchHelper(
    onFriendMoved: (from: Int, to: Int) -> Unit,
    onDragEnded: () -> Unit,
) : ItemTouchHelper(
    FriendsTouchHelperCallback(onFriendMoved, onDragEnded)
)