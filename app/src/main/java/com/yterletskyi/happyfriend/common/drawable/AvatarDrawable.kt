package com.yterletskyi.happyfriend.common.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.PaintDrawable
import androidx.annotation.ColorInt
import com.yterletskyi.happyfriend.common.x.sp

/**
 * @param cornerRadius - set [Float.MAX_VALUE] to make the drawable round
 */
class AvatarDrawable(
    private val text: String,
    private val textSize: Float = 18.sp,
    @ColorInt private val bgColor: Int = Color.parseColor("#FF6200EE"),
    cornerRadius: Float = Float.MAX_VALUE,
) : PaintDrawable(bgColor) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = this@AvatarDrawable.textSize
    }

    private val fontHeight by lazy { textPaint.fontMetrics.run { top + bottom }.toInt() }
    private val fontWidth by lazy { textPaint.measureText(text).toInt() }

    init {
        setCornerRadius(cornerRadius)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val h = bounds.height().toFloat()
        val w = bounds.width().toFloat()

        val x = (w - fontWidth) / 2f
        val y = (h - fontHeight) / 2f

        canvas.drawText(text, x, y, textPaint)
    }

    override fun getIntrinsicWidth(): Int = SIZE

    override fun getIntrinsicHeight(): Int = SIZE

    companion object {
        private const val SIZE = 100
    }
}
