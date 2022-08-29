package com.yterletskyi.happyfriend.features.settings.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

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

    companion object {
        private const val SEPARATE_LINE_POSITION = 8f
        private const val SEPARATE_LINE_INDENT = 45f
    }
}
