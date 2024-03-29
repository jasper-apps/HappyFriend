package com.jasperapps.happyfriend.features.settings.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.x.dp
import com.jasperapps.happyfriend.common.x.getThemeColor

class SettingsLineItemDecoration(
    private val context: Context,
    @ColorInt private val lineColor: Int = context.getThemeColor(R.attr.colorOnSurface),
    @Dimension private val lineWidth: Float = context.resources.getDimension(R.dimen.separator_line_width),
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = lineColor
        strokeWidth = lineWidth
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lastChild = parent[parent.childCount - 1]
        val width = lastChild.width.toFloat()
        val y = lastChild.y
        c.drawLine(
            SEPARATOR_MARGIN_HORIZONTAL,
            y - SEPARATOR_MARGIN_VERTICAL,
            width - SEPARATOR_MARGIN_HORIZONTAL,
            y - SEPARATOR_MARGIN_VERTICAL,
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
            adapter.itemCount - 2 -> outRect.bottom = SEPARATOR_MARGIN_VERTICAL
            adapter.itemCount - 1 -> outRect.top = SEPARATOR_MARGIN_VERTICAL
        }
    }

    companion object {
        private val SEPARATOR_MARGIN_VERTICAL = 16.dp
        private val SEPARATOR_MARGIN_HORIZONTAL = 16f.dp
    }
}
