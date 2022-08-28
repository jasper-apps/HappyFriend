package com.yterletskyi.happyfriend.features.settings.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

class SeparateLineItem : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = Color.parseColor("#d3d3d3")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val width = parent[parent.childCount - 1].width.toFloat()
        val y = parent[parent.childCount - 1].y
        c.drawLine(
            0f + separator_line_width,
            y - separator_line_height,
            width - separator_line_width,
            y - separator_line_height,
            paint
        )
    }

    companion object {
        const val separator_line_height = 8f
        const val separator_line_width = 45f
    }
}
