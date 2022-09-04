package com.yterletskyi.happyfriend.common.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.PaintDrawable
import androidx.annotation.ColorInt
import com.yterletskyi.happyfriend.common.x.sp

class AvatarInitialsDrawable(
    private val initials: String,
    private val textSize: Float = 18.sp,
    @ColorInt private val bgColor: Int = Color.parseColor("#3700B3"),
    @ColorInt private val textColor: Int = Color.parseColor("#ffffff"),
) : PaintDrawable(bgColor) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        typeface = Typeface.DEFAULT
        textSize = this@AvatarInitialsDrawable.textSize
    }

    private val fontHeight by lazy { textPaint.fontMetrics.run { top + bottom }.toInt() }
    private val fontWidth by lazy { textPaint.measureText(initials).toInt() }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val h = bounds.height().toFloat()
        val w = bounds.width().toFloat()

        val x = (w - fontWidth) / 2f
        val y = (h - fontHeight) / 2f

        canvas.drawText(initials, x, y, textPaint)
    }

    override fun getIntrinsicWidth(): Int = SIZE

    override fun getIntrinsicHeight(): Int = SIZE

    companion object {
        private const val SIZE = 100
    }
}
