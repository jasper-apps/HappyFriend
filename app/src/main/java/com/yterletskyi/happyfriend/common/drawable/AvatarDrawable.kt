package com.yterletskyi.happyfriend.common.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.yterletskyi.happyfriend.common.x.sp

class AvatarDrawable(
    private val text: String,
    private val isRound: Boolean = true,
    private val textSize: Float = 26.sp,
    @ColorInt private val bgColor: Int = Color.parseColor("#397BF3")
) : Drawable() {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = this@AvatarDrawable.textSize
    }

    private val bgPaint = Paint().apply {
        isAntiAlias = true
        color = bgColor
        style = Paint.Style.FILL
    }

    private val fontHeight by lazy { textPaint.fontMetrics.run { top + bottom }.toInt() }
    private val fontWidth by lazy { textPaint.measureText(text).toInt() }

    override fun draw(canvas: Canvas) {
        val h = bounds.height().toFloat()
        val w = bounds.width().toFloat()

        val rectF = RectF().apply {
            set(0f, 0f, w, h)
        }

        val corner = if (isRound) h / 2 else 0f

        canvas.drawRoundRect(rectF, corner, corner, bgPaint)

        val x = (w - fontWidth) / 2f
        val y = (h - fontHeight) / 2f

        canvas.drawText(text, x, y, textPaint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {}
}
