package com.yterletskyi.happyfriend.features.settings.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.RecyclerView

class SeparateLineItem : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = Color.rgb(211, 211, 211)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var width = 0f
        var y = 0f
        parent.forEachIndexed { index, view ->
            if (index == parent.childCount - 1) {
                width = view.width.toFloat()
                y = view.y
            }
        }
        c.drawLine(0f, y - 8f, width, y - 8f, paint)
    }
}
