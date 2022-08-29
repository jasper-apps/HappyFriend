package com.yterletskyi.happyfriend.features.settings.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.yterletskyi.happyfriend.common.x.dp

class SeparateLineItem : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = Color.parseColor("#E8E8E8")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lastChild = parent[parent.childCount - 1]
        val width = lastChild.width.toFloat()
        val y = lastChild.y
        c.drawLine(
            0f + SEPARATE_LINE_INDENT,
            y - SEPARATE_LINE_POSITION,
            width - SEPARATE_LINE_INDENT,
            y - SEPARATE_LINE_POSITION,
            paint
        )
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter ?: return

        when (parent.getChildAdapterPosition(view)) {
            adapter.itemCount - 2 -> outRect.bottom = 8.dp
            adapter.itemCount - 1 -> outRect.top = 8.dp
        }
    }

    companion object {
        private const val SEPARATE_LINE_POSITION = 8f
        private var SEPARATE_LINE_INDENT = 8.dp
    }
}
