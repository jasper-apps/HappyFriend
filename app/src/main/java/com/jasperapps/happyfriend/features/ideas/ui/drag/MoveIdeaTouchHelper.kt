package com.jasperapps.happyfriend.features.ideas.ui.drag

import androidx.recyclerview.widget.ItemTouchHelper

class MoveIdeaTouchHelper(
    onIdeaMoved: (from: Int, to: Int) -> Unit,
    onDragEnded: () -> Unit,
) : ItemTouchHelper(
    MoveIdeaTouchHelperCallback(onIdeaMoved, onDragEnded)
)
