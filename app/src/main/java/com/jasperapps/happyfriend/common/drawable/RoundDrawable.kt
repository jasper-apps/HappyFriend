package com.jasperapps.happyfriend.common.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable

class RoundDrawable(
    private val originalDrawable: Drawable
) : Drawable() {

    private val path = Path().apply {
        addCircle(SIZE / 2f, SIZE / 2f, SIZE / 2f, Path.Direction.CW)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        originalDrawable.bounds = bounds
    }

    override fun draw(canvas: Canvas) {
        canvas.clipPath(path)
        originalDrawable.draw(canvas)
    }

    override fun setAlpha(alpha: Int) {
        originalDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        originalDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return originalDrawable.opacity
    }

    override fun getIntrinsicWidth(): Int = SIZE

    override fun getIntrinsicHeight(): Int = SIZE

    companion object {
        private const val SIZE = 100
    }
}
