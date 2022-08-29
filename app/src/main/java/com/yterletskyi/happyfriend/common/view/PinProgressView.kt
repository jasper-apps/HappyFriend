package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.yterletskyi.happyfriend.common.x.dp

class PinProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var step: Int = 0
    var steps: Int = 4

    private val paint: Paint = Paint().apply {
        color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST -> CIRCLE_DIAMETER * steps + BETWEEN_CIRCLE_SPACE * (steps - 1)
            else -> throw IllegalArgumentException("Only wrap_content width is supported")
        }

        val measuredHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> CIRCLE_DIAMETER
            else -> throw IllegalArgumentException("Only wrap_content height is supported")
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val y = height / 2f
        var startX = CIRCLE_DIAMETER / 2f

        for (s in 0 until steps) {
            canvas.drawCircle(startX, y, CIRCLE_DIAMETER / 2f, paint)
            startX += CIRCLE_DIAMETER + BETWEEN_CIRCLE_SPACE
        }
    }

    companion object {
        private val CIRCLE_DIAMETER = 8.dp
        private val BETWEEN_CIRCLE_SPACE = 6.dp
    }
}