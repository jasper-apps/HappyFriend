package com.yterletskyi.happyfriend.features.pin.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.x.sp
import com.yterletskyi.happyfriend.databinding.ViewPinKeyboardButtonBinding

class PinKeyboardButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewPinKeyboardButtonBinding? = null
    private val binding: ViewPinKeyboardButtonBinding get() = _binding!!

    @ColorInt
    var btnBackgroundColor: Int = Color.BLACK
        set(value) {
            field = value
            setBackgroundColor(value)
        }

    var text: String = ""
        set(value) {
            field = value
            binding.textView.text = value
        }

    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            binding.textView.setTextColor(value)
        }

    var textSize: Float = 16.sp
        set(value) {
            field = value
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    var drawable: Drawable? = null
        set(value) {
            field = value
            binding.imageView.background = value
        }

    init {
        val inflater = LayoutInflater.from(context)
        _binding = ViewPinKeyboardButtonBinding.inflate(inflater, this)
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PinKeyboardButtonView,
            0,
            0
        ).apply {

            try {
                getColor(R.styleable.PinKeyboardButtonView_btnBackgroundColor, -1)
                    .takeIf { it != -1 }
                    ?.let { btnBackgroundColor = it }
                text = getString(R.styleable.PinKeyboardButtonView_android_text).orEmpty()
                getColor(R.styleable.PinKeyboardButtonView_android_textColor, -1)
                    .takeIf { it != -1 }
                    ?.let { textColor = it }
                getDimension(R.styleable.PinKeyboardButtonView_android_textSize, -1f)
                    .takeIf { it != -1f }
                    ?.let { textSize = it }
                getDrawable(R.styleable.PinKeyboardButtonView_android_src)
                    ?.let { drawable = it }
            } finally {
                recycle()
            }
        }
    }

    override fun setBackgroundColor(color: Int) {
        val roundColorDrawable = PaintDrawable(color)
            .also { it.setCornerRadius(Float.MAX_VALUE) }

        background = RippleDrawable(
            ColorStateList.valueOf(Color.TRANSPARENT),
            roundColorDrawable,
            roundColorDrawable
        )
    }
}
