package com.yterletskyi.happyfriend.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getColorOrThrow
import com.yterletskyi.happyfriend.R
import com.yterletskyi.happyfriend.common.x.sp
import com.yterletskyi.happyfriend.databinding.ViewPinKeyboardButtonBinding

class PinKeyboardButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var _binding: ViewPinKeyboardButtonBinding? = null
    private val binding: ViewPinKeyboardButtonBinding get() = _binding!!

    @ColorInt
    var btnBackgroundColor: Int = Color.BLACK
        set(value) {
            field = value
            val drawable = PaintDrawable(value)
                .also { it.setCornerRadius(Float.MAX_VALUE) }
            background = drawable
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
            binding.textView.textSize = value
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
                btnBackgroundColor = getColorOrThrow(
                    R.styleable.PinKeyboardButtonView_btnBackgroundColor
                )
                text = getString(R.styleable.PinKeyboardButtonView_android_text).orEmpty()
                textColor = getColorOrThrow(R.styleable.PinKeyboardButtonView_android_textColor)
                textSize = getFloat(R.styleable.PinKeyboardButtonView_android_textSize, textSize)
                drawable = getDrawable(R.styleable.PinKeyboardButtonView_android_src)
            } finally {
                recycle()
            }
        }
    }
}