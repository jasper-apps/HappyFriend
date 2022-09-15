package com.jasperapps.happyfriend.features.pin.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.getColorOrThrow
import com.jasperapps.happyfriend.R
import com.jasperapps.happyfriend.common.x.dp

class PinProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @ColorInt
    var defaultCircleColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var progressCircleColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    var step: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var steps: Int = 1
        set(value) {
            field = value
            invalidate()
        }

    private val defaultCirclePaint: Paint by lazy {
        Paint().apply { color = defaultCircleColor }
    }

    private val progressCirclePaint: Paint by lazy {
        Paint().apply { color = progressCircleColor }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PinProgressView,
            0,
            0
        ).apply {
            try {
                steps = getInt(R.styleable.PinProgressView_steps, steps)
                defaultCircleColor = getColorOrThrow(
                    R.styleable.PinProgressView_defaultCircleColor
                )
                progressCircleColor = getColorOrThrow(
                    R.styleable.PinProgressView_progressCircleColor
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recycle()
            }
        }
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
            val paint = if (s < step) progressCirclePaint else defaultCirclePaint
            canvas.drawCircle(startX, y, CIRCLE_DIAMETER / 2f, paint)
            startX += CIRCLE_DIAMETER + BETWEEN_CIRCLE_SPACE
        }
    }

    companion object {
        private val CIRCLE_DIAMETER = 12.dp
        private val BETWEEN_CIRCLE_SPACE = 12.dp
    }
}
